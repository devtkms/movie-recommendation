package com.devtkms.movierecommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WatchProviderResponseDto {
    private String providerName;
    private String logoPath;
}