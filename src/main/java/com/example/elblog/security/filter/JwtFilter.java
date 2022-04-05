package com.example.elblog.security.filter;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.example.elblog.entity.Blogger;
import com.example.elblog.exception.BadRequestException;
import com.example.elblog.security.service.dto.LoginUser;
import com.example.elblog.service.BloggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        if(token == null){
            filterChain.doFilter(request,response);
            return;
        }
        String servletPath = request.getServletPath();
        if (servletPath.contains("login")) {
            filterChain.doFilter(request,response);
            return;
        }
        JWT jwt = JWTUtil.parseToken(token);
        Integer userId = (Integer) jwt.getPayload("userId");
        LoginUser loginUser = (LoginUser) redisTemplate.opsForValue().get("blogger:" + userId);
        if (loginUser == null) {
            throw new AccountExpiredException("密码已过期，请重新登录");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);
    }
}
