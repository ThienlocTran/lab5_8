package com.thienloc.jakarta.lab58.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/api/employee-json")
public class JsonResponseServlet  extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws  SecurityException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> employee = new LinkedHashMap<>();
        employee.put("maNv","PS01");
        employee.put("hoTen", "Tran Thien Loc");
        employee.put("gioiTinh", true);
        employee.put("luong", 1000000);

        String json = mapper.writeValueAsString(employee);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(json);
        response.flushBuffer();

    }
}
