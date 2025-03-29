// 映画推薦系の責務を分離した4つのサービスクラスのひな形

package com.devtkms.movierecommendation.service.impl;

import com.devtkms.movierecommendation.service.AfterScoringService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

// AfterScoringServiceImpl.java
@Service
public class AfterScoringServiceImpl implements AfterScoringService {

    private static final Map<String, int[]> AFTER_GENRE_MAP = Map.of(
            "refresh", new int[]{28, 80, 37},
            "warm", new int[]{16, 10751, 10749},
            "cry", new int[]{18, 10752, 10749},
            "think", new int[]{99, 9648, 36}
    );

    @Override
    public Map<String, Integer> scoreGenresByAfterFeel(String after) {
        Map<String, Integer> scores = new HashMap<>();
        int[] genres = AFTER_GENRE_MAP.getOrDefault(after, new int[]{});
        for (int genreId : genres) {
            scores.put(String.valueOf(genreId), 2); // Q3は2点
        }
        return scores;
    }
}