package com.devtkms.movierecommendation.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a registered user.
 * Maps to the 'users' table.
 */
@Data
public class UserEntity {

    /** Internal user ID (primary key) */
    private Long id;

    /** Unique user ID (used for login) */
    private String userId;

    /** Encrypted password */
    private String password;

    /** Nickname of the user */
    private String nickname;

    /** Preferred streaming provider name (e.g., Netflix) */
    private String useProviderName;

    /** Preferred streaming provider ID (TMDb ID) */
    private Integer useProviderId;

    /** Favorite movie title */
    private String favoriteMovieName;

    /** Favorite movie ID (TMDb ID) */
    private Long favoriteMovieId;

    /** Gender */
    private String gender;

    /** Age group */
    private String ageGroup;

    /** Provider information */
    private String provider;

    /** Record creation timestamp */
    private LocalDateTime createdAt;

    /** Record last update timestamp */
    private LocalDateTime updatedAt;
}