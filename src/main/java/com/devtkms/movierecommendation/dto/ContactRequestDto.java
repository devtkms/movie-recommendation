package com.devtkms.movierecommendation.dto;

import lombok.Data;

/**
 * DTO for receiving inquiries from the contact form.
 */
@Data
public class ContactRequestDto {

    /** User's nickname (can be anonymous) */
    private String nickname;

    /** Inquiry category (e.g., Bug Report, Feature Request, etc.) */
    private String category;

    /** Inquiry message body */
    private String message;
}