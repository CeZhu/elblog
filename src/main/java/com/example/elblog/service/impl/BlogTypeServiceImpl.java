package com.example.elblog.service.impl;

import com.example.elblog.entity.BlogType;
import com.example.elblog.entity.BlogTypeExample;
import com.example.elblog.entity.Page;
import com.example.elblog.entity.vo.BlogTypeVO;
import com.example.elblog.mapper.BlogTypeMapper;
import com.example.elblog.service.BlogService;
import com.example.elblog.service.BlogTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogTypeServiceImpl implements BlogTypeService {
    @Resource
    private BlogTypeMapper blogTypeMapper;
    @Autowired
    private BlogService blogService;
    @Override
    public List<BlogType> listAll() {
        return blogTypeMapper.selectByExample(new BlogTypeExample());
    }


    @Override
    public Page<BlogType> listByPage(Integer pageNum, Integer pageSize) {
        int startRow = pageNum*pageSize;
        List<BlogType> blogTypes = blogTypeMapper.selectByPage(startRow, pageSize);
        long total = blogTypeMapper.countByExample(new BlogTypeExample());
        Page<BlogType> page = new Page<>();
        page.setPageSize(pageSize);
        page.setPageNum(pageNum);
        page.setContents(blogTypes);
        page.setTotal(total);
        return page;
    }

    @Override
    public List<BlogTypeVO> listWithCount() {
        List<BlogType> blogTypes = listAll();
        List<BlogTypeVO> result = new ArrayList<>();
        for (BlogType blogType : blogTypes) {
            BlogTypeVO vo = new BlogTypeVO();
            BeanUtils.copyProperties(blogType,vo);

            Map<String, Object> param = new HashMap<>();
            param.put("typeId",blogType.getId());
            long count = blogService.countByParam(param);
            vo.setCount(count);
            result.add(vo);
        }
        return result;
    }

    @Override
    public int addBlogType(BlogType blogType) {
        return blogTypeMapper.insert(blogType);
    }

    @Override
    public int updateBlogType(BlogType blogType) {
        return blogTypeMapper.updateByPrimaryKeySelective(blogType);
    }

    @Override
    public void deleteBlogType(Integer id) {
        blogTypeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteByIds(Integer[] ids) {
        blogTypeMapper.deleteByIds(ids);
    }
}
