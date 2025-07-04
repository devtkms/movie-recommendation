package com.devtkms.movierecommendation.dto;

import lombok.Data;

/**
 * DTO for user registration requests.
 */
@Data
public class UserRegisterRequestDto {

    /** Unique user ID (used for login) */
    private String userId;

    /** User password */
    private String password;

    /** User nickname */
    private String nickname;

    /** Preferred streaming provider name */
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
}