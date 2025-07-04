package com.devtkms.movierecommendation.dto;

import lombok.Data;

/**
 * DTO returned after a successful inquiry submission.
 */
@Data
public class ContactResponseDto {

    /** Success message */
    private String message = "Your inquiry has been submitted successfully.";
}