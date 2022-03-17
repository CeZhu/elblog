package com.example.elblog.entity.vo;

import com.example.elblog.entity.BlogType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class BlogVO {
    private Integer id;

    private String title;

    private String summary;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date releasedate;

    private Integer clickhit;

    private Integer replyhit;

    private BlogType blogType;

    private String keyword;

    private String content;
}
