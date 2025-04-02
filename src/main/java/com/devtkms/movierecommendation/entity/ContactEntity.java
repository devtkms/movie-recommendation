package com.devtkms.movierecommendation.entity;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ContactEntity {
    private Long contactId;
    private String nickname;
    private String category;
    private String message;
}