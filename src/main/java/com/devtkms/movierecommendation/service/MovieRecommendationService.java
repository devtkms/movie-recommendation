package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.client.TmdbApiClient;
import com.devtkms.movierecommendation.dto.MovieRecommendationRequestDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResponseDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResultDto;
import com.devtkms.movierecommendation.entity.QuestionButtonLogEntity;
import com.devtkms.movierecommendation.mapper.QuestionButtonLogMapper;
import com.devtkms.movierecommendation.mapper.TagMasterMapper;
import com.devtkms.movierecommendation.response.TmdbResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 映画推薦に関するビジネスロジックを担当するサービスクラス
 */
@Service
public class MovieRecommendationService {

    private final TagMasterMapper tagMasterMapper;
    private final TmdbApiClient tmdbApiClient;
    private final MovieSelectorService movieSelector;
    private final QuestionButtonLogMapper questionButtonLogMapper;

    public MovieRecommendationService(TagMasterMapper tagMasterMapper,
                                      TmdbApiClient tmdbApiClient,
                                      MovieSelectorService movieSelector,
                                      QuestionButtonLogMapper questionButtonLogMapper) {
        this.tagMasterMapper = tagMasterMapper;
        this.tmdbApiClient = tmdbApiClient;
        this.movieSelector = movieSelector;
        this.questionButtonLogMapper = questionButtonLogMapper;
    }

    /**
     * 3つの質問に基づいて映画を推薦するメイン処理
     *
     * @param requestDto ユーザーの質問回答（mood, tone, after）
     * @return 推薦された映画リストを含む結果DTO
     */
    public MovieRecommendationResultDto recommendMovies(MovieRecommendationRequestDto requestDto) {
        // 質問回答のログをDBに保存（後で分析可能）
        QuestionButtonLogEntity log = new QuestionButtonLogEntity();
        log.setMood(requestDto.getMood());
        log.setTone(requestDto.getTone());
        log.setAfter(requestDto.getAfter());
        questionButtonLogMapper.insert(log);

        // 質問の組み合わせからTMDbキーワードIDリストを取得
        List<String> keywordIds = tagMasterMapper.findKeywordIdsByConditions(
                requestDto.getMood(),
                requestDto.getTone(),
                requestDto.getAfter()
        );

        // TMDb APIを呼び出して映画データを取得
        TmdbResponse response = tmdbApiClient.fetchMoviesByKeywords(keywordIds);

        // レスポンスをDTOのリストに変換
        List<MovieRecommendationResponseDto> allMovies = response.toMovieDtoList();

        // 映画リストから重複なしで20件に絞り込み
        List<MovieRecommendationResponseDto> selectedMovies = movieSelector.selectUniqueMovies(allMovies, 20);

        // 推薦結果DTOとして返却
        return new MovieRecommendationResultDto(selectedMovies);
    }
}