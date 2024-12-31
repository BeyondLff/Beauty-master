package org.example.user.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.user.constant.CodeConstant;
import org.example.user.entity.User;
import org.example.user.exception.LoginException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    //根据用户id和用户名生成jwt令牌
    public static String createJwt(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        String jwt = Jwts.builder()
                .setClaims(claims)   //定义载荷
                .signWith(SignatureAlgorithm.HS256, "Beauty")
                .setExpiration(new Date(System.currentTimeMillis() + 12 * 3600 * 3600))
                .compact();
        return jwt;
    }

    //解析令牌的正确性
    public static Claims parseJwt(String jwt) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("Beauty")
                    .parseClaimsJws(jwt)
                    .getBody();
            return claims;
        } catch (Exception e) {
            throw new LoginException(CodeConstant.AUTHORITY_ERROR, "你没有权限访问资源");
        }

    }
}
