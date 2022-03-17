package com.example.elblog.elasticsearch.repository;

import com.example.elblog.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


/**
 * @author 朱策
 */
@Repository
public interface BlogRepository extends ElasticsearchRepository<Blog,Integer> {
    Page<Blog> findBlogsByTitleOrContentOrKeyword(String title, String content, String keyword, Pageable pageable);
}
