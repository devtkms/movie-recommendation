// 映画推薦系の責務を分離した4つのサービスクラスのひな形

package com.devtkms.movierecommendation.service;

import java.util.Map;

/**
 * Q2: 雰囲気ベースでジャンルスコアを計算
 */
public interface ToneScoringService {
    Map<String, Integer> scoreGenresByTone(String tone);
}