package com.bhasaka.newsportal.core.servlets;

import com.bhasaka.newsportal.core.services.NPUtilService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/newsportal/url/students")
public class CustomServlet extends SlingAllMethodsServlet {

    @Reference
    NPUtilService npUtilService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        ResourceResolver resolver =npUtilService.getResourceResolver(); //request.getResourceResolver();
        Resource students = resolver.getResource("/content/students");
        JsonArrayBuilder array=Json.createArrayBuilder();
        if(students !=null)
        {
            Iterator<Resource> student = students.listChildren();
            while(student.hasNext())
            {
                Resource studentObject = student.next();
                ValueMap properties = studentObject.getValueMap();
                JsonObjectBuilder studentJson= Json.createObjectBuilder();
                studentJson.add("studentId",properties.get("studentId",String.class));
                studentJson.add("firstName",properties.get("firstName",String.class));
                studentJson.add("lastName",properties.get("lastName",String.class));
                studentJson.add("branch",properties.get("branch",String.class));
                array.add(studentJson);
            }
        }

        response.setContentType("application/json");
        response.getWriter().write(array.build().toString());
    }

    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String studentId=request.getParameter("studentId");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String branch=request.getParameter("branch");

        ResourceResolver resolver = request.getResourceResolver();
        Resource students = resolver.getResource("/content/students");
        Resource student=resolver.getResource("/content/students/"+studentId);

        if(students !=null && student==null)
        {
            Map<String,Object> collegeStudents=new HashMap<>();
            collegeStudents.put("studentId",studentId);
            collegeStudents.put("firstName",firstName);
            collegeStudents.put("lastName",lastName);
            collegeStudents.put("branch",branch);
            resolver.create(students,studentId,collegeStudents);
            response.getWriter().write("Student created successfully with this Id:"+studentId);
            resolver.commit();
        }
        else {
            response.getWriter().write("Student already exist with this Id:"+studentId);
        }
    }

    protected void doPut(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String studentId=request.getParameter("studentId");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String branch=request.getParameter("branch");


        ResourceResolver resolver = request.getResourceResolver();
        Resource students = resolver.getResource("/content/students");
        Resource student=resolver.getResource("/content/students/"+studentId);

        if(students !=null && student != null)
        {
            ModifiableValueMap MVP = student.adaptTo(ModifiableValueMap.class);
            if(firstName != null)
                MVP.put("firstName",firstName);
            if(lastName != null)
                MVP.put("lastName",lastName);
            if(branch != null)
                MVP.put("branch",branch);

            response.getWriter().write("Student update Successfully with this Id:"+student);
            resolver.commit();
        }
        else {
            response.getWriter().write("Student not found with this Id:"+studentId+" to update ");
        }
    }

    protected void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        ResourceResolver resolver = request.getResourceResolver();
        String studentId = request.getParameter("studentId");
        Resource student = resolver.getResource("/content/students/"+studentId);

        if(student !=null)
        {
            resolver.delete(student);
            response.getWriter().write("Student deleted successfully with this ID:"+studentId);

            resolver.commit();

        }
        else {
            response.getWriter().write("Student not found with this ID:"+studentId+" to delete");
        }
    }

}
