package com.devtkms.movierecommendation.entity;

import lombok.Data;
import java.time.ZonedDateTime;

/**
 * Entity representing a contact inquiry.
 * Maps to the 'contact' table.
 */
@Data
public class ContactEntity {

    /** User nickname (can be anonymous) */
    private String nickname;

    /** Inquiry category (e.g., bug report, feature request) */
    private String category;

    /** Inquiry message content */
    private String message;
}