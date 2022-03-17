package com.example.elblog.service;

import com.example.elblog.entity.Comment;
import com.example.elblog.entity.CommentExample;
import com.example.elblog.entity.Page;

import java.util.List;

public interface CommentService {
    List<Comment> listComments(CommentExample example);
    void saveComment(Comment comment);
    Page<Comment> getByPage(Integer pageNum, Integer pageSize);
    void updateComment(Comment comment);
    void deleteById(Integer id);
    void batchUpdate(List<Comment> commentList);
    void batchDelete(Integer[] ids);
}
