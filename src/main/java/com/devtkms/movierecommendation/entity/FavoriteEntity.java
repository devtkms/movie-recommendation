package com.devtkms.movierecommendation.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 映画推薦質問に対するユーザーの回答ログを記録するエンティティ
 * question_button_log テーブルに対応
 */
@Data
public class FavoriteEntity {

    private Long id;
    private String userId;
    private Long movieId;
    private String title;
    private String posterPath;
}