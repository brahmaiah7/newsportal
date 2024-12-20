package com.bhasaka.newsportal.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class)
//@SlingServletPaths("/bin/newsportal/images")
@SlingServletResourceTypes(resourceTypes = "newsportal/new-images",
        extensions = {"txt","json"},
        methods = {"GET","POST","PUT","DELETE"},
        selectors = {"test","popular"})


public class ServletExample extends SlingAllMethodsServlet
{
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws  IOException {
        response.getWriter().write("Message from POST request");
    }

    @Override
    protected void doDelete( SlingHttpServletRequest request,  SlingHttpServletResponse response) throws IOException {
        response.getWriter().write("Message from Delete request");
    }

    @Override
    protected void doPut( SlingHttpServletRequest request, SlingHttpServletResponse response) throws  IOException {
        response.getWriter().write("Message from Update request");
    }

    @Override
    protected void doGet( SlingHttpServletRequest request,SlingHttpServletResponse response) throws  IOException {
        response.getWriter().write("Message from GET request");
    }
}
