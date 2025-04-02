package com.devtkms.movierecommendation.entity;

import lombok.Data;

/**
 * 映画推薦質問に対するユーザーの回答ログを記録するエンティティ
 * question_button_log テーブルに対応
 */
@Data
public class QuestionButtonLogEntity {

    /** 質問1：気分の選択（例：light, emotional） */
    private String mood;

    /** 質問2：映画の雰囲気（例：fast, deep） */
    private String tone;

    /** 質問3：観終わった後の気持ち（例：refresh, think） */
    private String after;

}