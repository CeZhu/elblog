package com.example.elblog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@MapperScan("com.example.elblog.mapper")
public class ElblogApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElblogApplication.class, args);
    }

}
