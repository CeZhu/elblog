package com.example.elblog.controller;

import com.example.elblog.entity.BlogType;
import com.example.elblog.entity.Page;
import com.example.elblog.entity.vo.BlogTypeVO;
import com.example.elblog.service.BlogService;
import com.example.elblog.service.BlogTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/blogType")
public class BlogTypeController {
    @Autowired
    private BlogTypeService blogTypeService;


    @GetMapping
    public ResponseEntity<Object> listBlogTypes(){
        List<BlogType> blogTypes = blogTypeService.listAll();
        return new ResponseEntity<>(blogTypes, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Object> listBlogTypeWithCount(){
        List<BlogTypeVO> list = blogTypeService.listWithCount();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<Object> listBlogTypesByPage(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        Page<BlogType> blogTypes = blogTypeService.listByPage(pageNum, pageSize);
        return new ResponseEntity<>(blogTypes,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addBlogType(@RequestBody @Valid BlogType blogType) {
        Integer id = blogType.getId();
        if (id != null) {
            blogTypeService.updateBlogType(blogType);
        }else{
            blogTypeService.addBlogType(blogType);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteBlogType(Integer id){
        blogTypeService.deleteBlogType(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteByIds")
    public ResponseEntity<Object> deleteByIds(@RequestBody Integer[] ids) {
        blogTypeService.deleteByIds(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
