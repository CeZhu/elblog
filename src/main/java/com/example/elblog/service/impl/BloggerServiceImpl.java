package com.example.elblog.service.impl;

import com.example.elblog.entity.Blogger;
import com.example.elblog.entity.vo.BloggerVO;
import com.example.elblog.mapper.BloggerMapper;
import com.example.elblog.service.BloggerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BloggerServiceImpl implements BloggerService {
    @Resource
    private BloggerMapper bloggerMapper;
    @Override
    public BloggerVO about() {
        Blogger blogger = bloggerMapper.selectByPrimaryKey(1);
        BloggerVO bloggerVo = new BloggerVO();
        BeanUtils.copyProperties(blogger,bloggerVo);
        return bloggerVo;
    }

    @Override
    public void update(Blogger blogger) {
        bloggerMapper.updateBlogger(blogger);
    }

    @Override
    public Blogger get() {
        return bloggerMapper.find();
    }

    @Override
    public Blogger selectById(Integer id) {
        return bloggerMapper.selectByPrimaryKey(id);
    }
}
