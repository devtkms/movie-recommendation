package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.dto.ContactRequestDto;
import com.devtkms.movierecommendation.dto.ContactResponseDto;
import com.devtkms.movierecommendation.entity.ContactEntity;
import com.devtkms.movierecommendation.mapper.ContactMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;

@Service
public class ContactService {

    private final ContactMapper contactMapper;

    public ContactService(ContactMapper contactMapper) {
        this.contactMapper = contactMapper;
    }

    public ContactResponseDto save(ContactRequestDto dto) {
        ContactEntity entity = new ContactEntity();
        entity.setNickname(dto.getNickname());
        entity.setCategory(dto.getCategory());
        entity.setMessage(dto.getMessage());
        entity.setCreatedAt(LocalDateTime.now());

        // データベースに保存
        contactMapper.insert(entity);

        return new ContactResponseDto();
    }
}