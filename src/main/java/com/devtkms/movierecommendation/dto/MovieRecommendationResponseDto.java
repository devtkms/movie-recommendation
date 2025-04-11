package com.devtkms.movierecommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TMDbから取得した映画情報のDTO（1件分）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieRecommendationResponseDto {

    /** 映画の一意なID（TMDb ID） */
    private Long id;

    /** タイトル */
    private String title;

    /** 概要（あらすじ） */
    private String overview;

    /** ポスター画像パス（TMDbベース） */
    private String posterPath;

    /** 公開日（yyyy-MM-dd） */
    private String releaseDate;

    /** この映画が保存されているかどうか（ログインユーザーのお気に入り） */
    private Boolean isSaved; // 初期値false
}