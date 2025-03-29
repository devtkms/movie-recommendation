package com.devtkms.movierecommendation.service.impl;

import com.devtkms.movierecommendation.dto.MovieRecommendationResponseDto;
import com.devtkms.movierecommendation.service.MovieSelectorService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieSelectorServiceImpl implements MovieSelectorService {

    @Override
    public List<MovieRecommendationResponseDto> selectTopMovies(
            Map<String, Integer> genreScores,
            List<MovieRecommendationResponseDto> allMovies,
            int limit
    ) {
        // スコアの合計を計算
        int totalScore = genreScores.values().stream().mapToInt(Integer::intValue).sum();

        // ジャンルごとの選出予定数を計算（スコア比で割り当て）
        Map<String, Integer> genreLimits = new HashMap<>();
        for (Map.Entry<String, Integer> entry : genreScores.entrySet()) {
            int count = (int) Math.round((entry.getValue() / (double) totalScore) * limit);
            genreLimits.put(entry.getKey(), count);
        }

        // 各ジャンルごとに映画を選出
        Map<String, List<MovieRecommendationResponseDto>> moviesByGenre = new HashMap<>();
        for (MovieRecommendationResponseDto movie : allMovies) {
            String genre = movie.getGenreId(); // ここで DTO の genreId を取得
            if (!moviesByGenre.containsKey(genre)) {
                moviesByGenre.put(genre, new ArrayList<>());
            }
            moviesByGenre.get(genre).add(movie);
        }

        List<MovieRecommendationResponseDto> selected = new ArrayList<>();
        Set<String> seenTitles = new HashSet<>();

        // ジャンルごとに映画を選出
        for (String genre : genreLimits.keySet()) {
            List<MovieRecommendationResponseDto> genreMovies = moviesByGenre.getOrDefault(genre, Collections.emptyList());

            // 映画リストをスコアでソート後にシャッフル
            genreMovies.sort(Comparator.comparingInt(MovieRecommendationResponseDto::getGenreScore).reversed()); // genreScoreで降順ソート
            Collections.shuffle(genreMovies); // シャッフル

            int max = genreLimits.get(genre);
            for (MovieRecommendationResponseDto movie : genreMovies) {
                if (selected.size() >= limit) break;
                if (!seenTitles.contains(movie.getTitle())) {
                    selected.add(movie);
                    seenTitles.add(movie.getTitle());
                }
                if (--max <= 0) break;
            }
        }

        // 万が一足りない分は残りから補完
        if (selected.size() < limit) {
            List<MovieRecommendationResponseDto> remaining = allMovies.stream()
                    .filter(m -> !seenTitles.contains(m.getTitle()))
                    .collect(Collectors.toList());
            Collections.shuffle(remaining); // シャッフルして補完
            for (MovieRecommendationResponseDto movie : remaining) {
                if (selected.size() >= limit) break;
                selected.add(movie);
            }
        }

        return selected;
    }
}