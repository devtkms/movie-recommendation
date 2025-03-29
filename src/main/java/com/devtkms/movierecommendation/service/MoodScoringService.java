// 映画推薦系の責務を分離した4つのサービスクラスのひな形

package com.devtkms.movierecommendation.service;

import java.util.List;
import java.util.Map;

/**
 * Q1: 気分ベースでジャンルスコアを計算
 */
public interface MoodScoringService {
    Map<String, Integer> scoreGenresByMood(String mood);
}