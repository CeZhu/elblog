package com.example.elblog.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.elblog.entity.Blog;
import com.example.elblog.entity.Comment;
import com.example.elblog.entity.CommentExample;
import com.example.elblog.entity.Page;
import com.example.elblog.exception.BadRequestException;
import com.example.elblog.mapper.BlogMapper;
import com.example.elblog.mapper.CommentMapper;
import com.example.elblog.mq.config.MqConfig;
import com.example.elblog.service.CommentService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 朱策
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentMapper commentMapper;

    @Resource
    private BlogMapper blogMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public List<Comment> listComments(CommentExample example) {
        return commentMapper.selectByExample(example);
    }

    @Override
    public void saveComment(Comment comment) {
        commentMapper.insertSelective(comment);
        addReplyHit(comment);
    }

    private void addReplyHit(Comment comment) {
        Integer id = comment.getBlog().getId();
        Blog blog = blogMapper.selectByPrimaryKey(id);
        if (blog == null) {
            return;
        }
        Integer replyhit = blog.getReplyhit();
        replyhit++;
        blog.setReplyhit(replyhit);
        blogMapper.updateByPrimaryKeySelective(blog);
        rabbitTemplate.convertAndSend(MqConfig.ES_EXCHANGE, MqConfig.ES_SAVE_ROUTINGKEY, JSON.toJSONString(blog));
    }

    @Override
    public Page<Comment> getByPage(Integer pageNum, Integer pageSize) {
        int startRow = pageNum * pageSize;
        List<Comment> comments = commentMapper.selectByPage(startRow, pageSize);
        long total = commentMapper.countByExample(new CommentExample());

        Page page = new Page();
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        page.setContents(comments);
        page.setTotal(total);
        return page;
    }

    @Override
    public void updateComment(Comment comment) {
        Integer id = comment.getId();
        if (id == null) {
            throw new BadRequestException("评论id为空！");
        }
        Comment dbComment = commentMapper.selectByPrimaryKey(id);
        if (dbComment == null) {
            throw new BadRequestException("该评论不存在！");
        }

        commentMapper.updateByPrimaryKeySelective(comment);
    }

    @Override
    public void deleteById(Integer id) {
        substractReplyHit(id);
        commentMapper.deleteByPrimaryKey(id);
    }

    private void substractReplyHit(Integer id) {
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
        rabbitTemplate.convertAndSend(MqConfig.ES_EXCHANGE, MqConfig.ES_SAVE_ROUTINGKEY, JSON.toJSONString(blog));
    }

    @Override
    public void batchUpdate(List<Comment> commentList) {
        commentMapper.updateByIds(commentList);
    }

    @Override
    public void batchDelete(Integer[] ids) {
        commentMapper.deleteByIds(ids);
    }
}
