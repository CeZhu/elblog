package com.example.elblog.entity.vo;

import com.example.elblog.entity.Blog;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
public class CommentVO {
    private Integer id;

    private String userip;

    private Blog blog;

    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date commentdate;

    private String state;
}
