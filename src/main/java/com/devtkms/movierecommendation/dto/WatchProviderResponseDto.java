package com.devtkms.movierecommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for response containing watch provider information.
 */
@Data
@AllArgsConstructor
public class WatchProviderResponseDto {

    /** Name of the streaming provider (e.g., Netflix, Prime Video) */
    private String providerName;

    /** Logo path of the provider (TMDb logo_path) */
    private String logoPath;
}