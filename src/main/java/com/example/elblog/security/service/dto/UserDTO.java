package com.example.elblog.security.service.dto;

import com.example.elblog.entity.Blogger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author 朱策
 */

public class UserDTO extends User {
    public UserDTO(Blogger blogger, Collection<? extends GrantedAuthority> authorities) {
        super(blogger.getUsername(),blogger.getPassword(),authorities);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
