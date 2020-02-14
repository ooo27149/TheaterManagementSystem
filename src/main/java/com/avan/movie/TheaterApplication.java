package com.avan.movie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class TheaterApplication {

    public static void main(String[] args) {

        SpringApplication.run(TheaterApplication.class, args);
    }

}
