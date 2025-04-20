package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.client.TmdbApiClient;
import com.devtkms.movierecommendation.dto.MovieRecommendationRequestDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResponseDto;
import com.devtkms.movierecommendation.dto.MovieRecommendationResultDto;
import com.devtkms.movierecommendation.entity.UserEntity;
import com.devtkms.movierecommendation.mapper.FavoriteMapper;
import com.devtkms.movierecommendation.mapper.QuestionButtonLogMapper;
import com.devtkms.movierecommendation.mapper.TagMasterMapper;
import com.devtkms.movierecommendation.mapper.UserMapper;
import com.devtkms.movierecommendation.response.TmdbResponse;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * 映画推薦に関するビジネスロジックを担当するサービスクラス
 */
@Service
public class MovieRecommendationService {

    private final TagMasterMapper tagMasterMapper;
    private final TmdbApiClient tmdbApiClient;
    private final MovieSelectorService movieSelector;
    private final UserMapper userMapper;
    private final FavoriteMapper favoriteMapper;
    private static final Logger logger = Logger.getLogger(MovieRecommendationService.class.getName());

    public MovieRecommendationService(TagMasterMapper tagMasterMapper,
                                      TmdbApiClient tmdbApiClient,
                                      MovieSelectorService movieSelector,
                                      QuestionButtonLogMapper questionButtonLogMapper,
                                      UserMapper userMapper,
                                      FavoriteMapper favoriteMapper) {
        this.tagMasterMapper = tagMasterMapper;
        this.tmdbApiClient = tmdbApiClient;
        this.movieSelector = movieSelector;
        this.userMapper = userMapper;
        this.favoriteMapper = favoriteMapper;
    }

    /**
     * 3つの質問に基づいて映画を推薦するメイン処理
     *
     * @param requestDto ユーザーの質問回答（mood, tone, after）
     * @return 推薦された映画リストを含む結果DTO
     */
    public MovieRecommendationResultDto recommendMovies(MovieRecommendationRequestDto requestDto, String userId) {

        logger.info("ユーザーID: " + userId + "今の気分: " + requestDto.getMood() + "映画の雰囲気: " + requestDto.getTone()
                + "観終わった後: " + requestDto.getAfter());

        List<String> keywordIds = tagMasterMapper.findKeywordIdsByConditions(
                requestDto.getMood(),
                requestDto.getTone(),
                requestDto.getAfter()
        );

        TmdbResponse response = tmdbApiClient.fetchMoviesByKeywords(keywordIds);
        List<MovieRecommendationResponseDto> allMovies = response.toMovieDtoList();
        List<MovieRecommendationResponseDto> selectedMovies = movieSelector.selectUniqueMovies(allMovies, 20);

        if (userId != null) {
            Set<Long> savedIds = new HashSet<>(favoriteMapper.selectMovieIdsByUserId(userId));
            for (MovieRecommendationResponseDto movie : selectedMovies) {
                if (savedIds.contains(movie.getId())) {
                    movie.setIsSaved(true);
                }
            }
        }

        return new MovieRecommendationResultDto(selectedMovies);
    }

    /**
     *
     *
     * @return トレンド映画のリストを含む結果DTO
     */
    public MovieRecommendationResultDto getPersonalizeMovies(Long userId, String usrId) {

        logger.info("ユーザーID: " + userId + "さんが、レコメンド機能を使用");

        UserEntity user = userMapper.findById(userId);
        Long favoriteMovieId = user.getFavoriteMovieId();

        // favoriteMovieId が未設定の場合はトレンドのみ返す
        if (favoriteMovieId == null) {
            TmdbResponse trendResponse = tmdbApiClient.fetchRandomTrendingMovies();
            List<MovieRecommendationResponseDto> trending = trendResponse.toMovieDtoList();
            return new MovieRecommendationResultDto(trending);
        }

        // レコメンド取得
        TmdbResponse recommendResponse = tmdbApiClient.fetchRecommendationsByMovieId(favoriteMovieId);

        // トレンド映画取得
        TmdbResponse trendResponse = tmdbApiClient.fetchRandomTrendingMovies();

        // DTOへ変換
        List<MovieRecommendationResponseDto> recommended = recommendResponse.toMovieDtoList();
        List<MovieRecommendationResponseDto> trending = trendResponse.toMovieDtoList();

        // 合体 & シャッフル
        recommended.addAll(trending);
        Collections.shuffle(recommended);

        if (userId != null) {
            Set<Long> savedIds = new HashSet<>(favoriteMapper.selectMovieIdsByUserId(usrId));
            for (MovieRecommendationResponseDto recommend : recommended) {
                if (savedIds.contains(recommend.getId())) {
                    recommend.setIsSaved(true);
                }
            }
        }

        return new MovieRecommendationResultDto(recommended);
    }
}
