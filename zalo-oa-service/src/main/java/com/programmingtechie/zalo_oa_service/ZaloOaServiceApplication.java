package com.programmingtechie.zalo_oa_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ZaloOaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZaloOaServiceApplication.class, args);
    }
}
