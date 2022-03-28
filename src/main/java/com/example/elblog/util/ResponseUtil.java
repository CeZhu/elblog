package com.example.elblog.util;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.ContentType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseUtil {
    public static void sendError(HttpServletResponse response, int status, String data) throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding(CharsetUtil.UTF_8);
        response.setContentType(ContentType.JSON.getValue());
        PrintWriter writer = response.getWriter();
        writer.write(data);
        writer.close();
    }
}
