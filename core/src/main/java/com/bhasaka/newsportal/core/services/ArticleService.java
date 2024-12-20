package com.bhasaka.newsportal.core.services;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component(service = ArticleService.class)
@Designate(ocd = ArticleConfiguration.class)
public class ArticleService
{
    private static final Logger LOG= LoggerFactory.getLogger("ArticleService.class");

    public String articleRestApiUrl;

    public boolean enable;

    public String clientId;

    @Activate
    public void activate(ArticleConfiguration configuration)
    {
        articleRestApiUrl=configuration.restApi();
        enable=configuration.enable();
        clientId=configuration.clientId();
        LOG.error("Inside Activate Method");
        LOG.info("Activate Rest API URL-{},enable-{},clientId-{}",articleRestApiUrl,enable,clientId);
    }

    @Deactivate
    public void deActivate(ArticleConfiguration configuration)
    {
        LOG.error("Inside DeActivate Method");
    }

    @Modified
    public void update(ArticleConfiguration configuration)
    {
        LOG.error("Inside Modified method");
        articleRestApiUrl=configuration.restApi();
        enable=configuration.enable();
        clientId=configuration.clientId();
        LOG.info("Modified Rest API URL-{},enable-{},clientId-{}",articleRestApiUrl,enable,clientId);
    }

    public String articles()
    {
        CloseableHttpClient client= HttpClients.createDefault();
        HttpGet getRequest=new HttpGet(articleRestApiUrl);

        try {
            CloseableHttpResponse response = client.execute(getRequest);
            if(response.getStatusLine().getStatusCode()==200)
            {
                return response.getEntity().getContent().toString();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "Welcome to Bhasaka Techologies";
    }

    public String testingMethod()
    {
        return "Welcome To Bhasaka Technologies";
    }

    public String getClientId() {
        return clientId;
    }

    public boolean isEnable() {
        return enable;
    }

    public String getArticleRestApiUrl() {
        return articleRestApiUrl;
    }
}
