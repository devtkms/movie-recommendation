package com.devtkms.movierecommendation.entity;

import lombok.Data;

import java.time.ZonedDateTime;

/**
 * お問い合わせ内容を表すエンティティ
 * contact テーブルに対応
 */
@Data
public class ContactEntity {

    /** ユーザーのニックネーム（匿名可） */
    private String nickname;

    /** 問い合わせカテゴリ（例：不具合報告、機能要望など） */
    private String category;

    /** 問い合わせ内容メッセージ */
    private String message;

}