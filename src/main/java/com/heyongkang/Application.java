package com.heyongkang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.heyongkang.mapper")
@EnableCaching
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("学生信息管理系统启动成功！");
        System.out.println("访问地址: http://localhost:8080");
        System.out.println("默认账号: admin, 密码: 123456");
    }
}