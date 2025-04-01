package com.devtkms.movierecommendation.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieRecommendationRequestDto {
    private String mood;                    // Q1: 気分
    private String tone;                    // Q2: 雰囲気
    private String after;                   // Q3: 見終わった後の気持ち
}