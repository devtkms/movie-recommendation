package com.devtkms.movierecommendation.service.impl;

import com.devtkms.movierecommendation.service.ToneScoringService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ToneScoringServiceImpl implements ToneScoringService {

    private static final Map<String, int[]> TONE_GENRE_MAP = Map.of(
            "slow", new int[]{10749, 18, 99},
            "fast", new int[]{28, 12, 35},
            "deep", new int[]{878, 14, 9648, 27},
            "casual", new int[]{10751, 10770}
    );

    @Override
    public Map<String, Integer> scoreGenresByTone(String tone) {
        Map<String, Integer> scores = new HashMap<>();
        int[] genres = TONE_GENRE_MAP.getOrDefault(tone, new int[]{});
        for (int genreId : genres) {
            scores.put(String.valueOf(genreId), 2); // Q2は2点
        }
        return scores;
    }
}