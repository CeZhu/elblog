package com.example.elblog.service.impl;


import com.example.elblog.elasticsearch.repository.BlogRepository;
import com.example.elblog.entity.Blog;
import com.example.elblog.service.ElasticsearchServie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 朱策
 */
@Service
public class ElasticsearchServiceImpl implements ElasticsearchServie {
    @Resource
    private BlogRepository blogRepository;

    @Override
    public Page<Blog> findByParam(String param, Integer pageNum, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        return blogRepository.findBlogsByTitleOrContentOrKeyword(param, param, param, pageRequest);
    }

}
