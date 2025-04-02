package com.devtkms.movierecommendation.mapper;

import com.devtkms.movierecommendation.entity.ContactEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * お問い合わせ情報の永続化を行うマッパーインターフェース
 */
@Mapper
public interface ContactMapper {

    /**
     * お問い合わせ内容をデータベースに保存する
     *
     * @param entity 保存対象のエンティティ（ニックネーム・カテゴリ・メッセージなど）
     */
    void insert(ContactEntity entity);
}