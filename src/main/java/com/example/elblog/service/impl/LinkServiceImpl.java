package com.example.elblog.service.impl;

import com.example.elblog.entity.Link;
import com.example.elblog.entity.LinkExample;
import com.example.elblog.entity.Page;
import com.example.elblog.mapper.LinkMapper;
import com.example.elblog.service.LinkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 朱策
 */
@Service
public class LinkServiceImpl implements LinkService {
    @Resource
    private LinkMapper linkMapper;
    @Override
    public Page<Link> getLinks(Integer pageNum, Integer pageSize) {
        int startRow = pageNum*pageSize;
        List<Link> links = linkMapper.selectByPage(startRow, pageSize);
        long count = linkMapper.countByExample(new LinkExample());

        Page<Link> page = new Page<>();
        page.setTotal(count);
        page.setPageSize(pageSize);
        page.setPageNum(pageNum);
        page.setContents(links);
        return page;
    }

    @Override
    public List<Link> listAll() {
        return linkMapper.selectByExample(new LinkExample());
    }

    @Override
    public void addLink(Link link) {
        linkMapper.insertSelective(link);
    }

    @Override
    public void editLink(Link link) {
        linkMapper.updateByPrimaryKey(link);
    }

    @Override
    public void deleteById(Integer id) {
        linkMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void batchDelete(Integer[] ids) {
        linkMapper.batchDelete(ids);
    }
}
