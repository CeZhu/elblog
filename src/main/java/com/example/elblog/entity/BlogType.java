package com.example.elblog.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class
BlogType {

    private Integer id;
    @NotBlank
    private String typename;
    @Positive
    private Integer orderno;
}