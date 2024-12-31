package org.example.user.service.Impl;

import com.aliyun.r_kvstore20150101.Client;
import com.aliyun.r_kvstore20150101.models.CreateCacheAnalysisTaskRequest;
import org.example.user.constant.CodeConstant;
import org.example.user.entity.LoginUser;
import org.example.user.entity.Result;
import org.example.user.exception.LoginException;
import org.example.user.mapper.UserMapper;
import org.example.user.request.LoginRequest;
import org.example.user.response.LoginResponse;
import org.example.user.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SecurityLoginService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    UserMapper userMapper;

    public LoginResponse Login(LoginRequest request) {
        try {
            if (userMapper.getUserByAccount(request.getAccount()) == null) {
                throw new LoginException(CodeConstant.USER_NOT_EXIST, "用户名不存在");
            }
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getAccount(), request.getPassword()));

            LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
            if(redisTemplate.opsForValue().get(loginUser.getUser().getId().toString())!=null){
                throw new LoginException(CodeConstant.ALREADY_LOGIN,"已经登录过，无需重复登录");
            }
            //添加键值对存放到redis当中
            redisTemplate.opsForValue().set
                    (loginUser.getUser().getId().toString(),loginUser.getUser().getRole(),3600, TimeUnit.SECONDS);
            return new LoginResponse(JwtUtils.createJwt(loginUser.getUser()));
        } catch (AuthenticationException e) {
            throw new LoginException(CodeConstant.PASSWORD_ERROR, "密码错误");
        }
    }
}
