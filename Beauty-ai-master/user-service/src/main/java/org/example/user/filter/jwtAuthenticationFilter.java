package org.example.user.filter;

import io.jsonwebtoken.Claims;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.user.constant.CodeConstant;
import org.example.user.entity.LoginUser;
import org.example.user.entity.User;
import org.example.user.exception.LoginException;
import org.example.user.handler.AccessDenied;
import org.example.user.mapper.UserMapper;
import org.example.user.utils.JwtUtils;
import org.example.user.utils.SpringUtils;
import org.example.user.utils.myThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Component
public class jwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    UserMapper userMapper;
    @Resource
    RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(userMapper==null){
            userMapper=SpringUtils.getBean(UserMapper.class);
        }
        if(redisTemplate==null){
            redisTemplate=SpringUtils.getBean("redisTemplate",RedisTemplate.class);
        }
        if(authHeader.equals("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }
        // 检查是否有Bearer前缀
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer")) {
            String token = authHeader.substring(7);
                Claims claims = JwtUtils.parseJwt(token);
                Integer userId = claims.get("id", Integer.class);
                myThreadLocal.set(userId);
            if (userId != null) {
                User user = userMapper.getUserById(userId);
                if (user != null) {
                    if(redisTemplate.opsForValue().get(userId.toString())!=null)
                    user.setRole((String)redisTemplate.opsForValue().get(userId.toString()));
                    else{
                        throw new AuthenticationException("还没登录!");
                    }
                    LoginUser loginUser = new LoginUser(user);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            loginUser, null, loginUser.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request,response);
                }
            }
        }
    }

}
