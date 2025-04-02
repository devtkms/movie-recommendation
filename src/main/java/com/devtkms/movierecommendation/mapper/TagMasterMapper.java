package com.devtkms.movierecommendation.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 質問の回答内容に応じたTMDbキーワードIDを取得するマッパー
 */
@Mapper
public interface TagMasterMapper {

    /**
     * 気分・雰囲気・観終わった気持ちの組み合わせに対応するキーワードIDを取得する
     *
     * @param mood  質問1：気分
     * @param tone  質問2：映画の雰囲気
     * @param after 質問3：観終わった後の気持ち
     * @return TMDbのキーワードIDリスト（文字列）
     */
    List<String> findKeywordIdsByConditions(String mood, String tone, String after);
}