package com.example.elblog.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 朱策
 */
@Data
@JsonIgnoreProperties(value = "handler")
@Document(indexName = "blog")
public class Blog implements Serializable {
    @Id
    private Integer id;

    @NotBlank(message = "标题不能为空")
    @Field(type = FieldType.Text)
    private String title;

    private String summary;

    private Date releasedate;

    private Integer clickhit;

    private Integer replyhit;
    @NotNull(message = "博客类型不能为空")
    private BlogType blogType;
    @NotBlank(message = "关键字不能为空")
    private String keyword;

    @NotBlank(message = "正文不能为空")
    @Field(type = FieldType.Text)
    private String content;
}