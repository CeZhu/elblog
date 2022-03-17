package com.example.elblog.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.elblog.constent.CommentConst;
import com.example.elblog.entity.Blog;
import com.example.elblog.entity.Comment;
import com.example.elblog.entity.CommentExample;
import com.example.elblog.entity.Page;
import com.example.elblog.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping
    public ResponseEntity<Object> getCommentList(Integer blogid) {
        CommentExample example = new CommentExample();
        example.createCriteria().andBlogidEqualTo(blogid).andStateEqualTo(1);
        example.setOrderByClause("commentDate desc");
        return new ResponseEntity<>(commentService.listComments(example), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> saveComment(@RequestBody Map<String, Object> data, HttpServletRequest request) throws InvocationTargetException, IllegalAccessException {
        System.out.println(data);
        String commentStr = JSON.toJSONString(data.get("comment"));
        Comment comment = JSON.parseObject(commentStr, Comment.class);
        // 设置blogid
        JSONObject jsonObject = JSON.parseObject(commentStr);
        Integer blogid = jsonObject.getInteger("blogid");
        Blog blog = new Blog();
        blog.setId(blogid);
        comment.setBlog(blog);

        String key = (String) data.get("key");
        String vCode = (String) data.get("vCode");
        String value = redisTemplate.opsForValue().get(key);
        if (value != null && value.equals(vCode.toLowerCase())) {
            String ip = request.getRemoteAddr();
            comment.setUserip(ip);
            comment.setState(CommentConst.UNCHECKED);
            comment.setCommentdate(new Date());
            commentService.saveComment(comment);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("/page")
    public ResponseEntity<Object> getCommentsByPage(Integer pageNum, Integer pageSize) {
        Page<Comment> page = commentService.getByPage(pageNum, pageSize);
        return new ResponseEntity<>(page,HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody Comment comment) {
        commentService.updateComment(comment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteById(Integer id){
        commentService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/batchUpdate")
    public ResponseEntity<Object> batchUpdate(@RequestBody List<Comment> commentList) {
        commentService.batchUpdate(commentList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/batchDelete")
    public ResponseEntity<Object> batchDelete(@RequestBody Integer[] ids) {
        commentService.batchDelete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
