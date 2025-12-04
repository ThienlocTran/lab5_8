package com.thienloc.jakarta.lab58.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/api/employee-json")
public class JsonResponseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        Map<String, Object> employee = new LinkedHashMap<>();
        employee.put("manv", "TeoNV");
        employee.put("hoTen", "Nguyễn Văn Tèo");
        employee.put("gioiTinh", true);
        employee.put("luong", 950.5);
        
        String json = mapper.writeValueAsString(employee);
        
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        resp.getWriter().print(json);
        resp.flushBuffer();
    }
}
