package com.example.elblog.controller;

import com.example.elblog.entity.Blogger;
import com.example.elblog.exception.BadRequestException;
import com.example.elblog.mapper.BloggerMapper;
import com.example.elblog.service.BloggerService;
import com.wf.captcha.SpecCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private PasswordEncoder encoder;

    @Resource
    private BloggerService bloggerService;

    @GetMapping("/captcha")
    public ResponseEntity<Object> captcha() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        String vCode = specCaptcha.text().toLowerCase();
        String key = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(key,vCode,2, TimeUnit.MINUTES);
        Map<String,String> imgResult = new HashMap<>();
        imgResult.put("key",key);
        imgResult.put("img",specCaptcha.toBase64());
        return new ResponseEntity<>(imgResult, HttpStatus.OK);
    }

    @PutMapping("/password")
    public ResponseEntity<Object> password(@RequestParam String oldPassword, @RequestParam String newPassword) {
        Blogger blogger = bloggerService.get();
        String password = blogger.getPassword();
        boolean matches = encoder.matches(oldPassword, password);
        if(!matches){
            throw new BadRequestException("旧密码错误");
        }
        blogger.setPassword(encoder.encode(newPassword));
        bloggerService.update(blogger);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
