package com.devtkms.movierecommendation.controller;

import com.devtkms.movierecommendation.dto.ContactRequestDto;
import com.devtkms.movierecommendation.dto.ContactResponseDto;
import com.devtkms.movierecommendation.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling contact form submissions.
 */
@RestController
@RequestMapping("/api/contact")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    /**
     * Endpoint for submitting contact form content.
     *
     * @param dto Contact form data sent from the frontend
     * @return Response message indicating the contact was received
     */
    @PostMapping("/submit")
    public ContactResponseDto submit(@RequestBody ContactRequestDto dto) {
        return contactService.save(dto);
    }

    /**
     * Endpoint for validating contact form input.
     *
     * @param dto Request DTO to validate
     * @return The same DTO after validation
     */
    @PostMapping("/validate")
    public ContactRequestDto validate(@Valid @RequestBody ContactRequestDto dto) {
        return dto;
    }
}