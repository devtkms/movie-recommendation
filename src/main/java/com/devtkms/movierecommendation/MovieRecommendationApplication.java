package com.devtkms.movierecommendation;

import com.devtkms.movierecommendation.handler.MovieRecommendationHandler;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MovieRecommendationApplication {
    public static void main(String[] args) {
        SpringApplication.run(MovieRecommendationApplication.class, args);

        new MovieRecommendationHandler();
    }

}
