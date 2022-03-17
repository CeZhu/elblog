package com.example.elblog.mapper;

import com.example.elblog.entity.Blog;
import com.example.elblog.entity.BlogExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BlogMapper {
    long countByExample(BlogExample example);

    long countByParam(Map<String,Object> param);

    int deleteByExample(BlogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Blog record);

    int insertSelective(Blog record);

    List<Blog> selectByExampleWithBLOBs(BlogExample example);

    List<Blog> selectByExample(BlogExample example);

    Blog selectByPrimaryKey(Integer id);

    List<Blog> selectByPage(Integer startRow, Integer pageSize);

    List<Blog> selectByParam(Map<String,Object> param);

    List<Map<String,Object>> sortByDate();

    int updateByExampleSelective(@Param("record") Blog record, @Param("example") BlogExample example);

    int updateByExampleWithBLOBs(@Param("record") Blog record, @Param("example") BlogExample example);

    int updateByExample(@Param("record") Blog record, @Param("example") BlogExample example);

    int updateByPrimaryKeySelective(Blog record);

    int updateByPrimaryKeyWithBLOBs(Blog record);

    int updateByPrimaryKey(Blog record);

    Blog getPrevBlog(Integer id);

    Blog getNextBlog(Integer id);

    void deleteByIds(Integer[] id);
}