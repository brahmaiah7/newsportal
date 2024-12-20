package com.bhasaka.newsportal.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/newsportal/employee")
public class EmployeeServlet extends SlingAllMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws  IOException {
        ResourceResolver resolver = request.getResourceResolver();
        Resource employees = resolver.getResource("/content/employees");
        JsonArrayBuilder array = Json.createArrayBuilder();

        if (employees != null) {
            Iterator<Resource> employeeIterator = employees.listChildren();
            while (employeeIterator.hasNext()) {
                Resource employeeResource = employeeIterator.next();
                ValueMap properties = employeeResource.getValueMap();
                JsonObjectBuilder employeeJson = Json.createObjectBuilder();
                employeeJson.add("empID", properties.get("empID", String.class));
                employeeJson.add("empName", properties.get("empName", String.class));
                employeeJson.add("email", properties.get("email", String.class));
                employeeJson.add("phoneNumber", properties.get("phoneNumber", String.class));
                array.add(employeeJson);
            }
            response.setStatus(SlingHttpServletResponse.SC_OK);
        } else {
            response.setStatus(SlingHttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("No employees found.");
        }

        response.setContentType("application/json");
        response.getWriter().write(array.build().toString());
    }

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws  IOException {
        String empID = request.getParameter("empId");
        String empName = request.getParameter("empName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");

        // Validate incoming parameters
        if (empID == null || empID.isEmpty()) {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Employee ID is required.");
            return;
        }
        if (empName == null || empName.isEmpty()) {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Employee Name is required.");
            return;
        }

        if (phoneNumber == null || phoneNumber.isEmpty()) {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Phone number is required.");
            return;
        }

        ResourceResolver resolver = request.getResourceResolver();
        Resource employees = resolver.getResource("/content/employees");

        if (employees == null) {
            employees = resolver.create(resolver.getResource("/content"), "employees", new HashMap<>());
        }

        Resource existingEmployee = resolver.getResource("/content/employees/" + empID);

        if (existingEmployee != null) {
            response.setStatus(SlingHttpServletResponse.SC_CONFLICT);
            response.getWriter().write("Employee with ID " + empID + " already exists.");
            return;
        }

        Map<String, Object> employeeData = new HashMap<>();
        employeeData.put("empID", empID);
        employeeData.put("empName", empName);
        employeeData.put("email", email);
        employeeData.put("phoneNumber", phoneNumber);

        resolver.create(employees, empID, employeeData);
        resolver.commit();

        response.setStatus(SlingHttpServletResponse.SC_OK);
        response.getWriter().write("Employee created successfully with ID: " + empID);
    }
}
