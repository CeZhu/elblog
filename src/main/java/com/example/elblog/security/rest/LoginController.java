package com.example.elblog.security.rest;

import cn.hutool.jwt.JWTUtil;
import com.example.elblog.entity.Blogger;
import com.example.elblog.exception.handler.ApiError;
import com.example.elblog.security.service.dto.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Value("${rsa.private-key}")
    private String key;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Blogger blogger) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(blogger.getUsername(), blogger.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        if (authentication == null) {
            return new ResponseEntity<>(ApiError.error("用户名或者密码错误"), HttpStatus.UNAUTHORIZED);
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Integer userId = loginUser.getBlogger().getId();
        Map<String, Object> map = new HashMap<>(10);
        map.put("userId", userId);
        String jwtToken = JWTUtil.createToken(map, key.getBytes());
        redisTemplate.opsForValue().set("blogger:" + userId, loginUser,1L, TimeUnit.DAYS);
        return new ResponseEntity<>(jwtToken, HttpStatus.OK);
    }

    @GetMapping("/sys/logout")
    public ResponseEntity<Object> logout() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer id = loginUser.getBlogger().getId();
        redisTemplate.delete("blogger:" + id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
