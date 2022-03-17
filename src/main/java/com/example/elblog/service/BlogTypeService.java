package com.example.elblog.service;

import com.example.elblog.entity.BlogType;
import com.example.elblog.entity.Page;
import com.example.elblog.entity.vo.BlogTypeVO;

import java.util.List;

public interface BlogTypeService {
    List<BlogType> listAll();
    Page<BlogType> listByPage(Integer pageNum, Integer pageSize);
    List<BlogTypeVO> listWithCount();
    int addBlogType(BlogType blogType);
    int updateBlogType(BlogType blogType);
    void deleteBlogType(Integer id);
    void deleteByIds(Integer[] ids);
}
