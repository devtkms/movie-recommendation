// MoodScoringServiceImpl.java
package com.devtkms.movierecommendation.service.impl;

import com.devtkms.movierecommendation.service.MoodScoringService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MoodScoringServiceImpl implements MoodScoringService {

    private static final Map<String, int[]> MOOD_GENRE_MAP = Map.of(
            "light", new int[]{35, 10751, 16, 12, 10402, 10770},
            "emotional", new int[]{18, 10749, 99, 36, 10752},
            "escape", new int[]{878, 14, 37},
            "thrill", new int[]{53, 27, 28, 80, 9648}
    );

    @Override
    public Map<String, Integer> scoreGenresByMood(String mood) {
        Map<String, Integer> scores = new HashMap<>();
        int[] genres = MOOD_GENRE_MAP.getOrDefault(mood, new int[]{});
        for (int genreId : genres) {
            scores.put(String.valueOf(genreId), 3); // Q1は3点
        }
        return scores;
    }
}