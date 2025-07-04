package com.devtkms.movierecommendation.dto;

import lombok.Data;

/**
 * DTO containing answers to the movie recommendation questions.
 */
@Data
public class MovieRecommendationRequestDto {

    /** Q1: Current mood (e.g., light, emotional) */
    private String mood;

    /** Q2: Desired movie tone (e.g., fast, deep) */
    private String tone;

    /** Q3: Desired feeling after watching (e.g., refresh, think) */
    private String after;

    /** Flag to exclude from analytics (true = developer's request) */
    private Boolean isMyData;
}