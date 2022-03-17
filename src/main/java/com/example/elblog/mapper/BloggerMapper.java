package com.example.elblog.mapper;

import com.example.elblog.entity.Blogger;
import com.example.elblog.entity.BloggerExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BloggerMapper {
    long countByExample(BloggerExample example);

    int deleteByExample(BloggerExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Blogger record);

    int insertSelective(Blogger record);

    List<Blogger> selectByExampleWithBLOBs(BloggerExample example);

    List<Blogger> selectByExample(BloggerExample example);

    Blogger selectByPrimaryKey(Integer id);

    Blogger find();

    int updateByExampleSelective(@Param("record") Blogger record, @Param("example") BloggerExample example);

    int updateByExampleWithBLOBs(@Param("record") Blogger record, @Param("example") BloggerExample example);

    int updateByExample(@Param("record") Blogger record, @Param("example") BloggerExample example);

    int updateByPrimaryKeySelective(Blogger record);

    int updateByPrimaryKeyWithBLOBs(Blogger record);

    int updateByPrimaryKey(Blogger record);

    void updateBlogger(Blogger blogger);
}