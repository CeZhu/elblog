package com.example.elblog.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
@Data
@JsonIgnoreProperties(value={"handler"})
public class Comment {
    private Integer id;

    private String userip;

    private Blog blog;

    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date commentdate;

    private Integer state;

}