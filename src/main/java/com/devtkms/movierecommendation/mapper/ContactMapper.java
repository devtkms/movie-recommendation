package com.devtkms.movierecommendation.mapper;

import com.devtkms.movierecommendation.entity.ContactEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper interface for persisting contact inquiries.
 */
@Mapper
public interface ContactMapper {

    /**
     * Inserts a contact inquiry into the database.
     *
     * @param entity The contact entity containing nickname, category, and message.
     */
    void insert(ContactEntity entity);
}