package com.example.english.interceptor;

import com.example.english.util.TokenUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    private TokenUtil tokenUtil;

    public HeaderInterceptor(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder();
        // 获取 Token
        String token = tokenUtil.getToken();

        // 添加 Token 到请求头
        if (token != null) {
            requestBuilder.header("Authorization", "Bearer " + token);
        }

        return chain.proceed(requestBuilder.build());
    }
}
