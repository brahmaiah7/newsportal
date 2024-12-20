package com.bhasaka.newsportal.core.services;

import org.apache.sling.api.resource.*;
import org.apache.sling.settings.SlingSettingsService;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Iterator;

@Component(service = CountryAPIService.class, immediate = true)
public class CountryAPIService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryAPIService.class);

    private static final String COUNTRIES_API_URL = "https://api.first.org/data/v1/countries";
    private static final String COUNTRIES_PATH = "/etc/countries";

    @Reference
    private NPUtilService npUtilService; // For ResourceResolver

    @Reference
    private SlingSettingsService slingSettingsService;

    @Activate
    protected void activate() {
        if (isPublishInstance()) {
            LOGGER.info("Running on Publish Instance. Starting to fetch and create country nodes...");
            fetchAndCreateCountryNodes();
        } else {
            LOGGER.info("Running on Author Instance. Skipping country node creation.");
        }
    }

    /**
     * Checks if the current instance is in Publish mode.
     */
    private boolean isPublishInstance() {
        return slingSettingsService.getRunModes().contains("publish");
    }

    /**
     * Fetch countries from API and create nodes in CRX under /etc/countries.
     */
    public void fetchAndCreateCountryNodes() {
        try (ResourceResolver resolver = npUtilService.getResourceResolver()) {
            if (resolver == null) {
                LOGGER.error("Resource Resolver is null! Please check NPUtilService configuration.");
                return;
            }

            // Get or create the /etc/countries root resource
            Resource parentResource = getOrCreateRootNode(resolver);
            if (parentResource == null) {
                LOGGER.error("Unable to create or access /etc/countries path.");
                return;
            }

            // Fetch country data
            JSONObject dataObject = fetchCountryData();

            // Iterate through country codes and names
            if (dataObject != null) {
                Iterator<String> keys = dataObject.keys();
                while (keys.hasNext()) {
                    String countryCode = keys.next();
                    String countryName = dataObject.getJSONObject(countryCode).getString("country");

                    createCountryNode(parentResource, countryCode, countryName, resolver);
                }
                resolver.commit();
                LOGGER.info("Country nodes created successfully under /etc/countries.");
            }
        } catch (Exception e) {
            LOGGER.error("Error while creating country nodes: ", e);
        }
    }

    /**
     * Fetch data from REST API.
     */
    private JSONObject fetchCountryData() {
        try {
            URL url = new URL(COUNTRIES_API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = connection.getInputStream()) {
                    String jsonResponse = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    return jsonObject.getJSONObject("data");
                }
            } else {
                LOGGER.error("Failed to fetch countries. HTTP code: {}", connection.getResponseCode());
            }
        } catch (Exception e) {
            LOGGER.error("Error while fetching country data: ", e);
        }
        return null;
    }

    /**
     * Create a node for a country under /etc/countries.
     */
    private void createCountryNode(Resource parent, String countryCode, String countryName, ResourceResolver resolver) {
        try {
            Resource countryNode = parent.getChild(countryCode);
            if (countryNode == null) {
                // Create a new node if it does not exist
                resolver.create(parent, countryCode, Collections.singletonMap("name", countryName));
                LOGGER.info("Created node for country: {} with name: {}", countryCode, countryName);
            } else {
                LOGGER.info("Node already exists for country: {}", countryCode);
            }
        } catch (PersistenceException e) {
            LOGGER.error("Error creating node for country: {}", countryCode, e);
        }
    }

    /**
     * Get or create the root node /etc/countries.
     */
    private Resource getOrCreateRootNode(ResourceResolver resolver) {
        Resource rootResource = resolver.getResource(COUNTRIES_PATH);
        if (rootResource == null) {
            try {
                rootResource = resolver.create(resolver.getResource("/etc"), "countries",
                        Collections.singletonMap(ResourceResolver.PROPERTY_RESOURCE_TYPE, "nt:unstructured"));
                resolver.commit();
                LOGGER.info("Created root node: /etc/countries");
            } catch (PersistenceException e) {
                LOGGER.error("Error creating root node /etc/countries: ", e);
            }
        }
        return rootResource;
    }
}
