package com.devtkms.movierecommendation.entity;

/**
 * 各質問パターンに紐づく TMDb キーワードIDを管理するエンティティ
 * tag_master テーブルに対応
 */
public class TagMasterEntity {

    /** 質問パターンの一意な識別子（例："light-fast-refresh"） */
    private String patternId;

    /** キーワードID群（カンマ区切りの文字列、例："123,456,789"） */
    private String keywordIds;

}