package com.example.elblog.security.service;

import com.example.elblog.entity.Blogger;
import com.example.elblog.entity.BloggerExample;
import com.example.elblog.mapper.BloggerMapper;
import com.example.elblog.security.service.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author 朱策
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private BloggerMapper bloggerMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BloggerExample example = new BloggerExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<Blogger> bloggers = bloggerMapper.selectByExample(example);
        if (bloggers.isEmpty()) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        Blogger blogger = bloggers.get(0);
        return new UserDTO(blogger, Collections.emptyList());
    }
}