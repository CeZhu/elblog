package com.example.elblog.service;

import com.example.elblog.entity.Blog;
import com.example.elblog.entity.Page;
import com.example.elblog.entity.vo.BlogVO;
import com.example.elblog.service.dto.BlogQueryCriteria;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * @author 朱策
 */
public interface BlogService {

    Page<BlogVO> listBlogs(Integer pageNum, Integer pageSize);

    Page<BlogVO> listByParam(Page page, BlogQueryCriteria blogQueryCriteria);

    Blog getBlogById(Integer id);

    Blog viewBlogById(Integer id);

    Blog getPrevBlog(Integer id);

    Blog getNextBlog(Integer id);

    List<Map<String,Object>> sortByDate();

    int addBlog(Blog blog);

    int updateBlog(Blog blog);

    void deleteById(Integer id);

    void deleteByIds(Integer[] ids);

    long countByParam(Map<String,Object> param);

    void download(HttpServletResponse response, BlogQueryCriteria criteria) throws IOException;
}
