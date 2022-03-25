package com.example.elblog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.example.elblog.mapper")
@EnableTransactionManagement
@EnableCaching
public class ElblogApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElblogApplication.class, args);
    }

}
