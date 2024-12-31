package org.example.user.handler;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.user.constant.CodeConstant;
import org.example.user.entity.Result;
import org.example.user.utils.webUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;



//用户认证异常处理器
public class AuthenticationEntryHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        authException.printStackTrace();
        Result result=Result.error(CodeConstant.IDENTIFY_ERROR,authException.getMessage());
        String json= JSON.toJSONString(result);
        webUtils.setResponse(response,json);
    }
}
