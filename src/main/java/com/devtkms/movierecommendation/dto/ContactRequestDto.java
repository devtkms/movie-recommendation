package com.devtkms.movierecommendation.dto;

import lombok.Data;

/**
 * お問い合わせフォームから受け取るリクエスト用DTO
 */
@Data
public class ContactRequestDto {

    /** ユーザーのニックネーム（匿名可） */
    private String nickname;

    /** お問い合わせカテゴリ（例: 不具合報告、機能要望など） */
    private String category;

    /** お問い合わせ本文 */
    private String message;
}