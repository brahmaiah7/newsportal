package com.bhasaka.newsportal.core.servlets;

import com.bhasaka.newsportal.core.services.NPUtilService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class WCMServletTest {

    @Mock
    NPUtilService npUtilService;

    AemContext context = new AemContext();

    @InjectMocks
    WCMServlet wcmServlet;

    MockSlingHttpServletRequest request;
    MockSlingHttpServletResponse response;

    @BeforeEach
    void init() {
        request = context.request();
        response = context.response();
        context.registerService(WCMServlet.class, wcmServlet);
        context.registerService(NPUtilService.class, npUtilService);
        context.create().page("/content/newsportal/us/en/templates");
    }

    @Test
    void testDoGet() throws ServletException, IOException {
        Mockito.when(npUtilService.getResourceResolver()).thenReturn(context.resourceResolver());
        context.create().page("/content/newsportal/us/en/templates/template-7","newsportal-project-template");
        context.create().page("/content/newsportal/us/en/templates/page-8");

        wcmServlet.doGet(request, response);


    }

    @Test
    void testDoPost() throws ServletException, IOException {
        request.addRequestParameter("pageName", "Page-1");
        request.addRequestParameter("pageTitle", "Page 1");

        wcmServlet.doPost(request, response);

       assertNotNull(context.resourceResolver().getResource("/content/newsportal/us/en/templates/Page-1"));
    }

    @Test
    void testDoDelete() throws ServletException, IOException {
        context.create().page("/content/newsportal/us/en/templates/Page-1");
        request.addRequestParameter("pageName", "Page-1");
        wcmServlet.doDelete(request,response);
        assertEquals(null,context.resourceResolver().getResource("/content/newsportal/us/en/templates/Page-1"));
    }
}
