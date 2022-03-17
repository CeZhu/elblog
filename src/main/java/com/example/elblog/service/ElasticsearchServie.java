package com.example.elblog.service;

import com.example.elblog.entity.Blog;
import org.springframework.data.domain.Page;


/**
 * @author 朱策
 */
public interface ElasticsearchServie {
    Page<Blog> findByParam(String param, Integer pageNum, Integer pageSize);
}
