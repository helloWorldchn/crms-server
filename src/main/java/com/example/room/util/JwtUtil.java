package com.example.room.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static String secret;
    private static Long expiration;
    private static String header;
    private static String tokenHead;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        JwtUtil.secret = secret;
    }

    @Value("${jwt.expiration}")
    public void setExpiration(Long expiration) {
        JwtUtil.expiration = expiration;
    }

    @Value("${jwt.header}")
    public void setHeader(String header) {
        JwtUtil.header = header;
    }

    @Value("${jwt.token-head}")
    public void setTokenHead(String tokenHead) {
        JwtUtil.tokenHead = tokenHead;
    }

    /**
     * 生成 token，强制将 userId 存入 claims
     * @param userId 用户ID
     * @param username 用户名（可选）
     * @return token字符串
     */
    public static String generateToken(Integer userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)                  // 自定义声明
                .setSubject(String.valueOf(userId))  // 主题也可以放 userId
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * 从 token 中解析 Claims
     */
    public static Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            // 日志记录异常，这里简单返回 null
            return null;
        }
    }

    /**
     * 从 token 中获取用户ID
     */
    public static Integer getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            // 从 claims 中获取 userId，注意类型转换
            return claims.get("userId", Integer.class);
        }
        return null;
    }

    /**
     * 从 HttpServletRequest 中获取 token（去除 Bearer 前缀）
     */
    public static String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(header);
        if (!StringUtils.isEmpty(authHeader) && authHeader.startsWith(tokenHead)) {
            return authHeader.substring(tokenHead.length()).trim();
        }
        // 兼容你原有的 X-Token 或 token
        if (StringUtils.isEmpty(authHeader)) {
            authHeader = request.getHeader("X-Token");
        }
        if (StringUtils.isEmpty(authHeader)) {
            authHeader = request.getHeader("token");
        }
        return authHeader;
    }

    /**
     * 从请求中直接获取当前用户ID（便捷方法）
     */
    public static Integer getUserIdByJwtToken(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token != null) {
            return getUserIdFromToken(token);
        }
        return null;
    }
}