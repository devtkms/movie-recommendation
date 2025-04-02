package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.dto.MovieRecommendationResponseDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class MovieSelectorService {
    public List<MovieRecommendationResponseDto> selectUniqueMovies(List<MovieRecommendationResponseDto> movies, int limit) {
        // ✅ mutableなリストに変換
        List<MovieRecommendationResponseDto> unique = new ArrayList<>(movies.stream()
                .distinct()
                .toList());

        // ✅ ランダムにシャッフル
        Collections.shuffle(unique);

        // ✅ 上位n件を返す
        return unique.stream()
                .limit(limit)
                .toList();
    }
}