package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.dto.MovieRecommendationRequestDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResponseDto;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieRecommendationService {

    private final MoodScoringService moodScoringService;
    private final ToneScoringService toneScoringService;
    private final AfterScoringService afterScoringService;
    private final MovieFetchService movieFetchService;
    private final MovieSelectorService movieSelectorService; // 追加

    public MovieRecommendationService(
            MoodScoringService moodScoringService,
            ToneScoringService toneScoringService,
            AfterScoringService afterScoringService,
            MovieFetchService movieFetchService,
            MovieSelectorService movieSelectorService // 追加
    ) {
        this.moodScoringService = moodScoringService;
        this.toneScoringService = toneScoringService;
        this.afterScoringService = afterScoringService;
        this.movieFetchService = movieFetchService;
        this.movieSelectorService = movieSelectorService; // 追加
    }

    public Map<String, List<MovieRecommendationResponseDto>> recommend(MovieRecommendationRequestDto requestDto) {
        // 各質問のジャンルスコア取得
        Map<String, Integer> moodScores = moodScoringService.scoreGenresByMood(requestDto.getMood());     // 3点
        Map<String, Integer> toneScores = toneScoringService.scoreGenresByTone(requestDto.getTone());     // 2点
        Map<String, Integer> afterScores = afterScoringService.scoreGenresByAfterFeel(requestDto.getAfter()); // 2点

        // ジャンルごとのスコアを合算
        Map<String, Integer> totalScores = new HashMap<>();

        mergeScores(totalScores, moodScores);
        mergeScores(totalScores, toneScores);
        mergeScores(totalScores, afterScores);

        // スコアが高い順にジャンルをソート
        List<String> topGenres = totalScores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 上位ジャンルから映画を取得（最大50件）
        List<MovieRecommendationResponseDto> combinedMovies = new ArrayList<>();
        Set<String> seenTitles = new HashSet<>();

        for (String genre : topGenres) {
            if (combinedMovies.size() >= 50) break;

            requestDto.setGenre(genre);
            List<MovieRecommendationResponseDto> movies = movieFetchService.fetchMovies(requestDto);

            for (MovieRecommendationResponseDto movie : movies) {
                if (!seenTitles.contains(movie.getTitle())) {
                    combinedMovies.add(movie);
                    seenTitles.add(movie.getTitle());
                }
                if (combinedMovies.size() >= 50) break;
            }
        }

        // MovieSelectorServiceで20件に絞る
        combinedMovies = movieSelectorService.selectTopMovies(totalScores, combinedMovies, 20);

        // シャッフル
        Collections.shuffle(combinedMovies);

        Map<String, List<MovieRecommendationResponseDto>> result = new HashMap<>();
        result.put("combined", combinedMovies);
        return result;
    }

    private void mergeScores(Map<String, Integer> total, Map<String, Integer> addition) {
        for (Map.Entry<String, Integer> entry : addition.entrySet()) {
            total.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }
    }
}