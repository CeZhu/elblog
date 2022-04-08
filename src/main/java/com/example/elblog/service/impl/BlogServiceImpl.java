package com.example.elblog.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.elblog.entity.Blog;
import com.example.elblog.entity.BlogExample;
import com.example.elblog.entity.Page;
import com.example.elblog.entity.vo.BlogVO;
import com.example.elblog.exception.BadRequestException;
import com.example.elblog.exception.EntityNotExistException;
import com.example.elblog.mapper.BlogMapper;
import com.example.elblog.mq.config.MqConfig;
import com.example.elblog.service.BlogService;
import com.example.elblog.service.dto.BlogQueryCriteria;
import com.example.elblog.util.FileUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


/**
 * @author 朱策
 */
@Service
@CacheConfig(cacheNames = "blog")
public class BlogServiceImpl implements BlogService {
    @Resource
    private BlogMapper blogMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AsyncServiceImpl asyncService;

    @Override
    @Cacheable(key = "#id")
    public Blog getBlogById(Integer id) {
        return blogMapper.selectByPrimaryKey(id);
    }

    @Override
    @CachePut(key = "#id")
    @Transactional(rollbackFor = Exception.class)
    public Blog viewBlogById(Integer id) {
        Blog blog = blogMapper.selectByPrimaryKey(id);
        asyncService.addBlogViewCount(blog);
        return blog;
    }

    @Override
    public Blog getPrevBlog(Integer id) {
        return blogMapper.getPrevBlog(id);
    }

    @Override
    public Blog getNextBlog(Integer id) {
        return blogMapper.getNextBlog(id);
    }

    @Override
    public List<Map<String, Object>> sortByDate() {
        return blogMapper.sortByDate();
    }

    @Override
    public int addBlog(Blog blog) {
        if (blog.getBlogType().getId() == null) {
            throw new BadRequestException("博客类型不能为空");
        }
        String content = blog.getContent();
        String summary = content.length() < 155 ? content : content.substring(0, 155);
        blog.setSummary(summary);
        blog.setClickhit(0);
        blog.setReplyhit(0);
        blog.setReleasedate(new Date());
        int insert = blogMapper.insert(blog);

        // 发消息存入es
//        rabbitTemplate.convertAndSend(MqConfig.ES_EXCHANGE, MqConfig.ES_SAVE_ROUTINGKEY, JSON.toJSONString(blog));

        return insert;
    }

    @Override
    @CacheEvict(key = "#blog.id")
    public int updateBlog(Blog blog) {
        Blog blog1 = blogMapper.selectByPrimaryKey(blog.getId());
        if (blog1 != null) {
            updateSummary(blog);
            int result = blogMapper.updateByPrimaryKeySelective(blog);

            // 发消息更新es
//            rabbitTemplate.convertAndSend(MqConfig.ES_EXCHANGE, MqConfig.ES_SAVE_ROUTINGKEY, JSON.toJSONString(blog));

            return result;
        } else {
            throw new EntityNotExistException("需更新的博客不存在");
        }
    }

    private void updateSummary(Blog blog) {
        if (blog == null) {
            return;
        }
        if (blog.getContent() == null || blog.getContent().length() == 0) {
            return;
        }

        String content = blog.getContent();
        String summary = content.length() < 155 ? content : content.substring(0, 155);
        blog.setSummary(summary);

    }

    @Override
    @CacheEvict(key = "#id")
    public void deleteById(Integer id) {
        blogMapper.deleteByPrimaryKey(id);
        // 在es中删除
//        rabbitTemplate.convertAndSend(MqConfig.ES_EXCHANGE, MqConfig.ES_DELETE_ROUTINGKEY, id);
    }

    @Override
    public void deleteByIds(Integer[] ids) {
        blogMapper.deleteByIds(ids);
        // 在es中删除
//        rabbitTemplate.convertAndSend(MqConfig.ES_EXCHANGE, MqConfig.ES_DELETEALL_ROUTINKEY, ids);
    }

    @Override
    public long countByParam(Map<String, Object> param) {
        return blogMapper.countByParam(param);
    }

    @Override
    public void download(HttpServletResponse response, BlogQueryCriteria criteria) throws IOException {
        List<BlogVO> blogVOS = listByParam(criteria);
        List<Map<String,Object>> list = new ArrayList<>();
        for (BlogVO content: blogVOS) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("编号",content.getId());
            map.put("标题",content.getTitle());
            map.put("发布日期",content.getReleasedate());
            map.put("博客类别",content.getBlogType().getTypename());
            list.add(map);
        }
        FileUtil.downloadExcel(list,response);
    }

    @Override
    public Page<BlogVO> listBlogs(Integer pageNum, Integer pageSize) {
        int startRow = pageNum * pageSize;
        List<Blog> blogs = blogMapper.selectByPage(startRow, pageSize);

        List<BlogVO> result = new ArrayList<>();
        blogs.forEach(blog -> {
            BlogVO vo = new BlogVO();
            BeanUtils.copyProperties(blog, vo);
            result.add(vo);
        });

        Page<BlogVO> page = new Page<>();
        page.setTotal(blogMapper.countByExample(new BlogExample()));
        page.setContents(result);
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        return page;
    }

    @Override
    public Page<BlogVO> listByParam(Page page, BlogQueryCriteria blogQueryCriteria) {
        Integer pageSize = page.getPageSize();
        Integer pageNum = page.getPageNum();
        int startRow = pageSize * pageNum;

        Map<String, Object> param = new HashMap<>();
        param.put("startRow", startRow);
        param.put("pageSize", pageSize);
        param.put("typeId", blogQueryCriteria.getTypeId());
        param.put("releaseDateStr", blogQueryCriteria.getReleaseDateStr());
        param.put("title", blogQueryCriteria.getTitle());
        param.put("startDate", blogQueryCriteria.getStartDate());
        param.put("endDate", blogQueryCriteria.getEndDate());

        List<Blog> blogs = blogMapper.selectByParam(param);

        List<BlogVO> result = new ArrayList<>();
        blogs.forEach(blog -> {
            BlogVO vo = new BlogVO();
            BeanUtils.copyProperties(blog, vo);
            result.add(vo);
        });

        page.setTotal(blogMapper.countByParam(param));
        page.setContents(result);

        return page;
    }

    public List<BlogVO> listByParam(BlogQueryCriteria blogQueryCriteria) {
        Map<String, Object> param = new HashMap<>();
        param.put("typeId", blogQueryCriteria.getTypeId());
        param.put("releaseDateStr", blogQueryCriteria.getReleaseDateStr());
        param.put("title", blogQueryCriteria.getTitle());
        param.put("startDate", blogQueryCriteria.getStartDate());
        param.put("endDate", blogQueryCriteria.getEndDate());
        List<Blog> blogs = blogMapper.selectByParam(param);

        List<BlogVO> result = new ArrayList<>();
        blogs.forEach(blog -> {
            BlogVO vo = new BlogVO();
            BeanUtils.copyProperties(blog, vo);
            result.add(vo);
        });

        return result;
    }

}
