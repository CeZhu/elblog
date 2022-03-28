package com.example.elblog.security.service;

import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.example.elblog.exception.handler.ApiError;
import com.example.elblog.util.ResponseUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ApiError error = ApiError.error("用户权限不足");
        ResponseUtil.sendError(response, HttpStatus.HTTP_FORBIDDEN, JSON.toJSONString(error));
    }
}
