package com.example.elblog.service.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author 朱策
 */
@Data
public class BlogQueryCriteria {

    private String title;

    private String summary;

    private Date releaseDate;

    private Integer typeId;

    private String keyword;

    private String content;

    private String releaseDateStr;

    private String startDate;

    private String endDate;
}
