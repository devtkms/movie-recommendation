package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.dto.ContactRequestDto;
import com.devtkms.movierecommendation.dto.ContactResponseDto;
import com.devtkms.movierecommendation.entity.ContactEntity;
import com.devtkms.movierecommendation.mapper.ContactMapper;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * お問い合わせに関するビジネスロジックを担当するサービスクラス
 */
@Service
public class ContactService {

    private final ContactMapper contactMapper;

    public ContactService(ContactMapper contactMapper) {
        this.contactMapper = contactMapper;
    }

    /**
     * お問い合わせ情報を保存する
     *
     * @param dto フロントエンドから送られたお問い合わせ内容
     * @return 保存完了後のレスポンスDTO（現在は空）
     */
    public ContactResponseDto save(ContactRequestDto dto) {
        // DTOの値をEntityに変換
        ContactEntity entity = new ContactEntity();
        entity.setNickname(dto.getNickname());
        entity.setCategory(dto.getCategory());
        entity.setMessage(dto.getMessage());

        // createdAtなどはMyBatisのMapperで自動付与される想定（またはDB側のデフォルト）

        // DBに問い合わせ情報を保存
        contactMapper.insert(entity);

        // TODO: 必要に応じてレスポンス内容をセットする（IDや登録時刻など）
        return new ContactResponseDto();
    }
}