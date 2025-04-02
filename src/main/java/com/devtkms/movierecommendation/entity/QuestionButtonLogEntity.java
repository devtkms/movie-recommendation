package com.devtkms.movierecommendation.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class QuestionButtonLogEntity {
    private String mood;
    private String tone;
    private String after;
}