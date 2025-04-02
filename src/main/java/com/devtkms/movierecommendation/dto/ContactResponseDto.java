package com.devtkms.movierecommendation.dto;

import lombok.Data;

/**
 * お問い合わせ送信完了後に返却されるレスポンスDTO
 */
@Data
public class ContactResponseDto {

    /** 成功メッセージ */
    private String message = "お問い合わせが送信されました";
}