package com.example.pcr_search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // 스케쥴러 연결하는 어노테이션
@SpringBootApplication
public class PcrSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(PcrSearchApplication.class, args);
    }

}
