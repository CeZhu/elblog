package com.example.elblog.controller;

import com.example.elblog.entity.Blogger;
import com.example.elblog.service.BloggerService;
import com.example.elblog.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/blogger")
public class BloggerController {
    @Autowired
    private BloggerService bloggerService;

    @GetMapping("/about")
    public ResponseEntity<Object> about() {
        return new ResponseEntity<>(bloggerService.about(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Object> update(String username, String nickname,
                                         String sign, String profile,
                                         MultipartFile file, HttpServletRequest request) {
        Blogger blogger = new Blogger();
        blogger.setUsername(username);
        blogger.setNickname(nickname);
        blogger.setSign(sign);
        blogger.setProfile(profile);

        if (file != null && !file.isEmpty()) {
            String uploadPath = FileUtil.upload(file, request);
            blogger.setImagename(uploadPath);
        }

        bloggerService.update(blogger);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
