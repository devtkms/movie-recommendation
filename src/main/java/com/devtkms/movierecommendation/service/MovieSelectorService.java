package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.dto.MovieRecommendationResponseDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 映画リストから重複を除き、ランダムに指定数選定するユーティリティサービス
 */
@Component
public class MovieSelectorService {

    /**
     * 与えられた映画リストから重複を除去し、ランダムにlimit件選択して返す
     *
     * @param movies 映画推薦候補リスト（重複含む可能性あり）
     * @param limit  最終的に返却したい件数
     * @return 重複のないランダムな映画リスト（最大limit件）
     */
    public List<MovieRecommendationResponseDto> selectUniqueMovies(List<MovieRecommendationResponseDto> movies, int limit) {
        // 元のリストから重複を除いた新しいリストを作成（distinct()で重複排除）
        List<MovieRecommendationResponseDto> unique = new ArrayList<>(movies.stream()
                .distinct()
                .toList());

        // 映画リストをランダムに並び替え
        Collections.shuffle(unique);

        // 指定件数に絞って返却（最大limit件）
        return unique.stream()
                .limit(limit)
                .toList();
    }
}