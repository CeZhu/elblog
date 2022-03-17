package com.example.elblog.elasticsearch.controller;

import com.example.elblog.entity.Blog;
import com.example.elblog.service.ElasticsearchServie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 朱策
 */
@Slf4j
@RestController
@RequestMapping("/search")
public class ElasticsearchController {

    @Autowired
    private ElasticsearchServie elasticsearchServie;

    @GetMapping
    public ResponseEntity<Object> search(@RequestParam("searchParam") String param, Integer pageNum, Integer pageSize) {
        log.info("param:{}, pageNum:{}, pageSize:{}", param, pageNum, pageSize);
        Page<Blog> byParam = elasticsearchServie.findByParam(param, pageNum, pageSize);
        com.example.elblog.entity.Page<Blog> result = new com.example.elblog.entity.Page<>();
        result.setContents(byParam.getContent());
        result.setTotal(byParam.getTotalElements());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
