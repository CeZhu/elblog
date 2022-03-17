package com.example.elblog.service;

import com.example.elblog.entity.Blogger;
import com.example.elblog.entity.vo.BloggerVO;


public interface BloggerService {
    BloggerVO about();
    void update(Blogger blogger);
    Blogger get();
}
