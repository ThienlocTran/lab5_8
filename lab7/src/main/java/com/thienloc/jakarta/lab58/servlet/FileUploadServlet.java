package com.thienloc.jakarta.lab58.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/api/upload")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 50
)
public class FileUploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Part filePart = req.getPart("file");
            String fileName = filePart.getSubmittedFileName();
            String contentType = filePart.getContentType();
            long fileSize = filePart.getSize();
            
            String uploadDir = getServletContext().getRealPath("/uploads");
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }
            
            String filePath = uploadDir + File.separator + fileName;
            filePart.write(filePath);
            
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("name", fileName);
            result.put("type", contentType);
            result.put("size", fileSize);
            
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(result);
            
            resp.setCharacterEncoding("utf-8");
            resp.setContentType("application/json");
            resp.getWriter().print(json);
            resp.flushBuffer();
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
