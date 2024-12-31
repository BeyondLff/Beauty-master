package org.example.user.handler;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.user.constant.CodeConstant;
import org.example.user.entity.Result;
import org.example.user.utils.webUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class AccessDenied implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Result result=Result.error(CodeConstant.AUTHORITY_ERROR,"权限不足");
        String json= JSON.toJSONString(result);
        webUtils.setResponse(response,json);
    }
}
