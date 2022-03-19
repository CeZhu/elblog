package com.example.elblog.elasticsearch.controller;

import com.example.elblog.entity.Page;
import com.example.elblog.entity.vo.BlogVO;
import com.example.elblog.service.BlogService;
import com.example.elblog.service.dto.BlogQueryCriteria;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class ElasticsearchController {

    @Resource
    private BlogService blogService;
    @GetMapping("/search")
    public Page<BlogVO> search(String searchParam, Page page) {
        BlogQueryCriteria criteria = new BlogQueryCriteria();
        criteria.setTitle(searchParam);
        Page<BlogVO> blogVOPage = blogService.listByParam(page, criteria);

        Map<String,Object> map = new HashMap<>();
        map.put("title",searchParam);
        map.put("pageNum",page.getPageNum());
        map.put("pageSize",page.getPageSize());
        long total = blogService.countByParam(map);
        blogVOPage.setTotal(total);
        return blogVOPage;
    }

}
