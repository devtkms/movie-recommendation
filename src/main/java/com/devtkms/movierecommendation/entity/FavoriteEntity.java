package com.devtkms.movierecommendation.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entity representing a saved movie by a user.
 * Maps to the 'favorites' table.
 */
@Data
public class FavoriteEntity {

    /** Internal ID of the favorite record */
    private Long id;

    /** ID of the user who saved the movie */
    private String userId;

    /** TMDb ID of the saved movie */
    private Long movieId;

    /** Movie title */
    private String title;

    /** Poster image path */
    private String posterPath;
}