package com.bhasaka.newsportal.core.servlets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/newsportal/users")
public class UserDataServlet extends SlingAllMethodsServlet {

    private static final String USERS_PATH = "/etc/users";

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        StringBuilder jsonString = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
        }
        if (jsonString.length() == 0) {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Empty JSON body.");
            return;
        }

        JsonObject userData;
        try {
            userData = JsonParser.parseString(jsonString.toString()).getAsJsonObject();
        } catch (Exception e) {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid JSON format: " + e.getMessage());
            return;
        }

        String firstName = userData.has("fName") ? userData.get("fName").getAsString() : "";
        String lastName = userData.has("lName") ? userData.get("lName").getAsString() : "";
        String userID = userData.has("email") ? userData.get("email").getAsString() : "";
        String password = userData.has("password") ? userData.get("password").getAsString() : "";

        if (firstName.isEmpty() || userID.isEmpty() || password.isEmpty()) {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("User FirstName, ID, and Password are required.");
            return;
        }

        ResourceResolver resolver = request.getResourceResolver();
        Resource usersFolder = resolver.getResource(USERS_PATH);
        if (usersFolder == null) {
            usersFolder = resolver.create(resolver.getResource("/etc"), "users", new HashMap<>());
            resolver.commit();
        }

        Resource existingUser = resolver.getResource(USERS_PATH + "/" + userID);
        if (existingUser != null) {
            response.setStatus(SlingHttpServletResponse.SC_CONFLICT);
            response.getWriter().write("User with ID " + userID + " already exists.");
        } else {
            Map<String, Object> userProps = new HashMap<>();
            userProps.put("firstName", firstName);
            userProps.put("lastName", lastName);
            userProps.put("userID", userID);
            userProps.put("password", password);

            resolver.create(usersFolder, userID, userProps);
            resolver.commit();

            response.setStatus(SlingHttpServletResponse.SC_OK);
            response.getWriter().write("User created successfully with Name: " + firstName);
        }
    }
}
