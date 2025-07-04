package com.devtkms.movierecommendation.entity;

/**
 * Entity that manages TMDb keyword IDs associated with each question pattern.
 * Maps to the 'tag_master' table.
 */
public class TagMasterEntity {

    /** Unique identifier for the question pattern (e.g., "light-fast-refresh") */
    private String patternId;

    /** Comma-separated list of TMDb keyword IDs (e.g., "123,456,789") */
    private String keywordIds;
}