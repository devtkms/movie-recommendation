package com.devtkms.movierecommendation.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
public class ContactEntity {
    private Long contactId;
    private String nickname;
    private String category;
    private String message;
    private LocalDateTime createdAt;  // ZonedDateTime型に変更
}