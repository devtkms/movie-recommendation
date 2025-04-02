package com.devtkms.movierecommendation.dto;

import lombok.Data;

/**
 * 配信サービス取得リクエストDTO
 */
@Data
public class WatchProviderRequestDto {

    /** 対象となる映画ID（TMDb ID） */
    private Long movieId;
}