package com.example.server.manager;

import com.example.server.manager.common.util.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class StartApplication {

    public static void main(String[] args) {
        ApplicationContext act = SpringApplication.run(StartApplication.class, args);
        SpringUtil.setApplicationContext(act);
    }
}
