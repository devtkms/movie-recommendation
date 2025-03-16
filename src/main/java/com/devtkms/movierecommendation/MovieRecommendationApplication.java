package com.devtkms.movierecommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MovieRecommendationApplication {
    public static void main(String[] args) {
        SpringApplication.run(MovieRecommendationApplication.class, args);
    }

}
