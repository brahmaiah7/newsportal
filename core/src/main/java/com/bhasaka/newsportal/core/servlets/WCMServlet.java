package com.bhasaka.newsportal.core.servlets;

import com.bhasaka.newsportal.core.services.NPUtilService;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Iterator;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/content/wcm")
public class WCMServlet extends SlingAllMethodsServlet {

    @Reference
    NPUtilService npUtilService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        ResourceResolver resolver = npUtilService.getResourceResolver();
        PageManager pageManager=resolver.adaptTo(PageManager.class);
        Page templates = pageManager.getPage("/content/newsportal/us/en/templates");

        JsonArrayBuilder arrayPages= Json.createArrayBuilder();

        if(templates != null)
        {
            Iterator<Page> pages = templates.listChildren();

            while(pages.hasNext())
            {

                Page page = pages.next();
                JsonObjectBuilder pagesJson=Json.createObjectBuilder();
                pagesJson.add("title",page.getTitle());
                pagesJson.add("page",page.getPath());
                arrayPages.add(pagesJson);
            }
        }
        response.setContentType("application/json");
        response.getWriter().write(arrayPages.build().toString());

    }

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        ResourceResolver resolver = request.getResourceResolver();
        PageManager pageManager=resolver.adaptTo(PageManager.class);

        String pageName=request.getParameter("pageName");
        String pageTitle=request.getParameter("pageTitle");

        try {
            pageManager.create("/content/newsportal/us/en/templates",pageName,"/conf/newsportal/settings/wcm/templates/newsportal-project-template",pageTitle);
        } catch (WCMException e) {
            throw new RuntimeException(e);
        }
        resolver.commit();
        response.getWriter().write("Page Created succesfully check in JCR repository");
    }

    @Override
    protected void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

        ResourceResolver resolver=request.getResourceResolver();
        PageManager pageManager=resolver.adaptTo(PageManager.class);
        String pageName= request.getParameter("pageName");
        Page page = pageManager.getPage("/content/newsportal/us/en/templates/"+pageName);
        try {
            if(page != null)
            {
                pageManager.delete(page,false);
                response.getWriter().write("Page deleted Successfully");
            }
        } catch (WCMException e) {
            throw new RuntimeException(e);
        }

    }
}
