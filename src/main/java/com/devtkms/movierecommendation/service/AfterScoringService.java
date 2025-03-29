// 映画推薦系の責務を分離した4つのサービスクラスのひな形

package com.devtkms.movierecommendation.service;

import java.util.Map;

/**
 * Q3: 見終わった後の気持ちベースでジャンルスコアを計算
 */
public interface AfterScoringService {
    Map<String, Integer> scoreGenresByAfterFeel(String after);
}