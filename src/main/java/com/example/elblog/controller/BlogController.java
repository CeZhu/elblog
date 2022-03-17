package com.example.elblog.controller;


import com.example.elblog.entity.Blog;
import com.example.elblog.entity.Page;
import com.example.elblog.service.BlogService;
import com.example.elblog.service.dto.BlogQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * @author 朱策
 */
@RestController
@RequestMapping("/blog")
@Validated
public class BlogController {
    @Autowired
    public BlogService blogService;

    @GetMapping
    public ResponseEntity<Object> getBlogs(Page page, BlogQueryCriteria blogQueryCriteria) {
        return new ResponseEntity<>(blogService.listByParam(page, blogQueryCriteria), HttpStatus.OK);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Object> viewBlogById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(blogService.viewBlogById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getBlogById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(blogService.getBlogById(id), HttpStatus.OK);
    }

    @GetMapping("/prev")
    public ResponseEntity<Object> getPrevBlog(@RequestParam("id") Integer id) {
        return new ResponseEntity<>(blogService.getPrevBlog(id), HttpStatus.OK);

    }

    @GetMapping("/next")
    public ResponseEntity<Object> getNextBlog(@RequestParam("id") Integer id) {
        return new ResponseEntity<>(blogService.getNextBlog(id), HttpStatus.OK);
    }

    @GetMapping("/sortByDate")
    public ResponseEntity<Object> sortByDate() {
        return new ResponseEntity<>(blogService.sortByDate(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addBlog(@RequestBody @Valid Blog blog) {
        if (blog.getId() == null) {
            blogService.addBlog(blog);
        } else {
            blogService.updateBlog(blog);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteBlog(@RequestParam("id") Integer id) {
        blogService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteBlogs")
    public ResponseEntity<Object> deleteBlogs(@RequestBody Integer[] ids) {
        blogService.deleteByIds(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response, BlogQueryCriteria blogQueryCriteria) throws IOException {
        blogService.download(response, blogQueryCriteria);
    }
}
