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
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private AsyncServiceImpl asyncService;

    @Override
    public List<Comment> listComments(CommentExample example) {
        return commentMapper.selectByExample(example);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveComment(Comment comment) {
        commentMapper.insertSelective(comment);
        asyncService.addReplyHit(comment);
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
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        asyncService.subtractReplyHit(id);
        commentMapper.deleteByPrimaryKey(id);
    }


    @Override
    public void batchUpdate(List<Comment> commentList) {
        commentMapper.updateByIds(commentList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Integer[] ids) {
        commentMapper.deleteByIds(ids);
    }
}
