package com.devtkms.movierecommendation.dto;

import lombok.Data;

/**
 * 映画推薦リクエストで送信される質問の回答内容を保持するDTO
 */
@Data
public class MovieRecommendationRequestDto {

    /** Q1: 今の気分（例: light, emotional） */
    private String mood;

    /** Q2: 映画の雰囲気（例: fast, deep） */
    private String tone;

    /** Q3: 見終わった後の気持ち（例: refresh, think） */
    private String after;
}