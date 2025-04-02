package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.dto.ContactRequestDto;
import com.devtkms.movierecommendation.dto.ContactResponseDto;
import com.devtkms.movierecommendation.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * お問い合わせフォームに関するRESTコントローラー
 */
@RestController
@RequestMapping("/api/contact")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    /**
     * お問い合わせ内容の送信エンドポイント
     *
     * @param dto フロントから送信されるお問い合わせ内容
     * @return お問い合わせ受付メッセージ
     */
    @PostMapping("/submit")
    public ContactResponseDto submit(@RequestBody ContactRequestDto dto) {
        return contactService.save(dto);
    }

    /**
     * お問い合わせ内容のバリデーション確認用エンドポイント
     * - 主に開発時のバリデーションチェックやフォーム確認に使用
     * - 実運用では通常不要（または管理者向け）
     *
     * @param dto バリデーション対象のリクエストDTO
     * @return バリデーション後のリクエストDTO（そのまま返却）
     */
    @PostMapping("/validate")
    public ContactRequestDto validate(@Valid @RequestBody ContactRequestDto dto) {
        return dto;
    }
}