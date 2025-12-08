package com.thienloc.jakarta.lab58.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

public class RestIO {
    static final private ObjectMapper mapper = new ObjectMapper();
    // Đọc JSON từ request body
    public static String readJson(HttpServletRequest req) throws IOException {
        req.setCharacterEncoding("UTF-8");
        BufferedReader reader = req.getReader();
        String line ;
        StringBuffer buffer = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        reader.close();
        return buffer.toString();
    }

    //Gửi JSON response
    public static void writeJson(HttpServletResponse resp, String json) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().write(json);
        resp.flushBuffer();

    }
    //Đọc JSON và convert to Object
    public static <T> T readObject(HttpServletRequest req, Class<T> clazz) throws IOException {
        String json = RestIO.readJson(req);
        // hoac co the khai bao T bean = cai do sau do return bean
        return mapper.readValue(json, clazz);
    }
   // Convert Object to JSON và gửi
    public static void writeObject(HttpServletResponse resp, Object data) throws IOException {
        String json = mapper.writeValueAsString(data);
        RestIO.writeJson(resp, json);
    }
    //Gửi empty object {}
    public static void writeEmptyObject(HttpServletResponse resp) throws IOException {
        RestIO.writeJson(resp, "{}");
    }
}
