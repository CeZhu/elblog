package com.example.elblog.mapper;

import com.example.elblog.entity.BlogType;
import com.example.elblog.entity.BlogTypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BlogTypeMapper {
    long countByExample(BlogTypeExample example);

    int deleteByExample(BlogTypeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BlogType record);

    int insertSelective(BlogType record);

    List<BlogType> selectByExample(BlogTypeExample example);

    BlogType selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BlogType record, @Param("example") BlogTypeExample example);

    int updateByExample(@Param("record") BlogType record, @Param("example") BlogTypeExample example);

    int updateByPrimaryKeySelective(BlogType record);

    int updateByPrimaryKey(BlogType record);

    List<BlogType> selectByPage(Integer startRow, Integer pageSize);

    void deleteByIds(Integer[] ids);
}