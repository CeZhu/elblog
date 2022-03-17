package com.example.elblog.entity;

import lombok.Data;

import java.util.List;

/**
 * @author 朱策
 */
@Data
public class Page <T>{
    private Integer pageNum;
    private Integer pageSize;
    private Long total;
    private List<T> contents;
}
