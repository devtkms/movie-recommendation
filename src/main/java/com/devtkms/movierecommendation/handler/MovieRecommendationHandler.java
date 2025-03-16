package com.devtkms.movierecommendation.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.devtkms.movierecommendation.MovieRecommendationApplication;

public class MovieRecommendationHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {

    private static final SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            // ✅ `SpringBootLambdaContainerHandler` を使用
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(MovieRecommendationApplication.class);
        } catch (Exception e) {
            throw new IllegalStateException("Lambda ハンドラーの初期化に失敗しました", e);
        }
    }

    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest awsProxyRequest, Context context) {
        return handler.proxy(awsProxyRequest, context);
    }
}