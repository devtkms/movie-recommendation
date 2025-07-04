package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.dto.ContactRequestDto;
import com.devtkms.movierecommendation.dto.ContactResponseDto;
import com.devtkms.movierecommendation.entity.ContactEntity;
import com.devtkms.movierecommendation.mapper.ContactMapper;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Service class responsible for handling contact form business logic.
 */
@Service
public class ContactService {

    private final ContactMapper contactMapper;

    public ContactService(ContactMapper contactMapper) {
        this.contactMapper = contactMapper;
    }

    /**
     * Save contact information to the database.
     *
     * @param dto Contact form data submitted from frontend
     * @return Response DTO after saving (currently empty)
     */
    public ContactResponseDto save(ContactRequestDto dto) {
        // Convert DTO to Entity
        ContactEntity entity = new ContactEntity();
        entity.setNickname(dto.getNickname());
        entity.setCategory(dto.getCategory());
        entity.setMessage(dto.getMessage());

        // Note: createdAt will be set automatically by MyBatis or DB defaults

        // Save to DB
        contactMapper.insert(entity);

        return new ContactResponseDto();
    }
}