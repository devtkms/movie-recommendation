package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.dto.ContactRequestDto;
import com.devtkms.movierecommendation.dto.ContactResponseDto;
import com.devtkms.movierecommendation.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/submit")
    public ContactResponseDto submit(@RequestBody ContactRequestDto dto) {
        return contactService.save(dto);
    }

    @PostMapping("/validate")
    public ContactRequestDto validate(@Valid @RequestBody ContactRequestDto dto) {
        return dto;
    }
}
