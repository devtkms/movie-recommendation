package com.devtkms.movierecommendation.mapper;

import com.devtkms.movierecommendation.entity.QuestionButtonLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 質問ボタンの選択ログを記録するマッパー
 */
@Mapper
public interface QuestionButtonLogMapper {

    /**
     * ユーザーが選択した質問の組み合わせをログとして保存する
     *
     * @param entity mood, tone, after の選択肢を含んだエンティティ
     */
    void insert(QuestionButtonLogEntity entity);
}