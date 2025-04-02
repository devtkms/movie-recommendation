package com.devtkms.movierecommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 配信サービス情報を返却するレスポンスDTO
 */
@Data
@AllArgsConstructor
public class WatchProviderResponseDto {

    /** 配信サービス名（例: Netflix, Prime Video） */
    private String providerName;

    /** サービスロゴ画像パス（TMDbのlogo_path） */
    private String logoPath;
}