package com.example.elblog.security.service.dto;

import cn.hutool.core.collection.CollectionUtil;
import com.example.elblog.entity.Blogger;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class LoginUser implements UserDetails {
    private Blogger blogger;
    private List<String> permissions;
    private List<SimpleGrantedAuthority> authorities;

    public LoginUser(Blogger blogger, List<String> permissions) {
        this.blogger = blogger;
        this.permissions = permissions;
    }

    @Override
    public List<SimpleGrantedAuthority> getAuthorities() {
        if (CollectionUtil.isEmpty(authorities)) {
            authorities = permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return blogger.getPassword();
    }

    @Override
    public String getUsername() {
        return blogger.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
