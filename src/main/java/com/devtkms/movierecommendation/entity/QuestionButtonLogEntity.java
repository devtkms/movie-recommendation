package com.devtkms.movierecommendation.entity;

import lombok.Data;

/**
 * Entity that logs user responses to movie recommendation questions.
 * Maps to the 'question_button_log' table.
 */
@Data
public class QuestionButtonLogEntity {

    /** Q1: User's current mood (e.g., light, emotional) */
    private String mood;

    /** Q2: Movie tone (e.g., fast, deep) */
    private String tone;

    /** Q3: Feeling after watching (e.g., refresh, think) */
    private String after;
}