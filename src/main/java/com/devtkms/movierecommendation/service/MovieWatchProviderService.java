package com.devtkms.movierecommendation.service;

import com.devtkms.movierecommendation.client.TmdbApiClient;
import com.devtkms.movierecommendation.dto.WatchProviderResponseDto;
import com.devtkms.movierecommendation.response.TmdbWatchProviderResponse;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 映画の配信サービス（Watch Providers）を取得・整形するサービスクラス
 */
@Service
public class MovieWatchProviderService {

    private final TmdbApiClient tmdbApiClient;

    public MovieWatchProviderService(TmdbApiClient tmdbApiClient) {
        this.tmdbApiClient = tmdbApiClient;
    }

    /**
     * 指定された映画IDに対する日本国内の配信サービスを取得する
     *
     * @param movieId TMDb映画ID
     * @return 対応する配信サービスのリスト（アイコンパス付き）
     */
    public List<WatchProviderResponseDto> getWatchProviders(Long movieId) {
        // TMDb APIから配信サービス情報を取得
        TmdbWatchProviderResponse response = tmdbApiClient.fetchWatchProviders(movieId);

        // 日本（JP）における配信情報のみ抽出
        var jp = response.getResults().get("JP");

        // 情報がなければ空リストを返却
        if (jp == null || jp.getFlatrate() == null) return Collections.emptyList();

        // 表示対象とする配信サービス名（ホワイトリスト）
        List<String> allowedProviders = List.of(
                "Amazon Prime Video",
                "Netflix",
                "Hulu",
                "U-NEXT",
                "Disney Plus"
        );

        // 対象配信サービスのみ抽出し、DTOに変換して返却
        return jp.getFlatrate().stream()
                .filter(p -> allowedProviders.contains(p.getProviderName()))
                .map(p -> new WatchProviderResponseDto(p.getProviderName(), p.getLogoPath()))
                .toList();
    }
}