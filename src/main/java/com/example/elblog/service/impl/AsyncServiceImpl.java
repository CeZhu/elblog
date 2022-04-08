package com.example.elblog.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.elblog.entity.Blog;
import com.example.elblog.entity.Comment;
import com.example.elblog.mapper.BlogMapper;
import com.example.elblog.mapper.CommentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author 朱策
 */
@Service
@Slf4j
public class AsyncServiceImpl {

    @Resource
    private BlogMapper blogMapper;

    @Resource
    private CommentMapper commentMapper;

    @Async
    @Transactional(rollbackFor = Exception.class)
    public void addBlogViewCount(Blog source) {
        log.info("增加博客点击,当前线程：{}",Thread.currentThread().getName());
        Blog blog = new Blog();
        BeanUtils.copyProperties(source, blog);
        Integer clickhit = source.getClickhit();
        clickhit++;
        blog.setClickhit(clickhit);
        blogMapper.updateByPrimaryKeySelective(blog);
    }

    @Async
    @Transactional(rollbackFor = Exception.class)
    public void addReplyHit(Comment comment) {
        Integer id = comment.getBlog().getId();
        Blog blog = blogMapper.selectByPrimaryKey(id);
        if (blog == null) {
            return;
        }
        Integer replyhit = blog.getReplyhit();
        replyhit++;
        blog.setReplyhit(replyhit);
        blogMapper.updateByPrimaryKeySelective(blog);
    }

    @Async
    @Transactional(rollbackFor = Exception.class)
    public void subtractReplyHit(Integer id) {
        Comment comment = commentMapper.selectByPrimaryKey(id);
        Integer blogId = comment.getBlog().getId();
        Blog blog = blogMapper.selectByPrimaryKey(blogId);
        if (blog == null) {
            return;
        }
        Integer replyhit = blog.getReplyhit();
        replyhit = replyhit > 0 ? replyhit - 1 : 0;
        blog.setReplyhit(replyhit);
        blogMapper.updateByPrimaryKeySelective(blog);
    }


}
