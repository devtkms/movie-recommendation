package com.devtkms.movierecommendation.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.HashMap;
import java.util.Map;

public class MovieRecommendationHandler implements RequestHandler<Map<String, String>, Map<String, String>> {

    @Override
    public Map<String, String> handleRequest(Map<String, String> input, Context context) {
        context.getLogger().log("Received input: " + input);

        // 仮のレスポンス
        Map<String, String> response = new HashMap<>();
        response.put("message", "映画のおすすめを取得しました！");
        response.put("status", "success");

        return response;
    }
}
