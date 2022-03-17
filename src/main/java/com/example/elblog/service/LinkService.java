package com.example.elblog.service;

import com.example.elblog.entity.Link;
import com.example.elblog.entity.Page;

import java.util.List;

public interface LinkService {
    Page<Link> getLinks(Integer pageNum, Integer pageSize);
    List<Link> listAll();
    void addLink(Link link);
    void editLink(Link link);
    void deleteById(Integer id);
    void batchDelete(Integer[] ids);
}
