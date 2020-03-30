package com.mss.framework.base.user.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.mss.framework.base"})
@MapperScan(basePackages = {"com.mss.framework.base.user.admin.dao"})
public class UserAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserAdminApplication.class, args);
    }
}
