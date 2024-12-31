package org.example.user.utils;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class webUtils {
    public static String setResponse(HttpServletResponse response,String string) throws IOException {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
