package com.xdx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class App {
    public static void main(String[] args) {
        int i = 100_1000_00;
        SpringApplication.run(App.class,args);
    }
}
