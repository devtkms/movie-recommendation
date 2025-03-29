package com.devtkms.movierecommendation.dto;

import lombok.Data;

@Data
public class ContactRequestDto {
    private String nickname;
    private String category;
    private String message;
}
