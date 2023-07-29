package com.example.english.interceptor;

import android.content.Intent;
import android.os.Looper;

import com.example.english.activity.LoginActivity;
import com.example.english.util.MyApplication;
import com.example.english.util.ResponseUtil;
import com.example.english.util.ToastUtil;
import com.example.english.util.TokenUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class TokenInterceptor implements Interceptor {

    private TokenUtil tokenUtil = new TokenUtil(MyApplication.getContext());

    public TokenInterceptor(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    // 跳转要修改
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            try {
                String result = buffer.clone().readString(StandardCharsets.UTF_8);
                Gson gson = new Gson();
                ResponseUtil responseUtil = gson.fromJson(result, ResponseUtil.class);
                int code = responseUtil.getCode();
                String msg = responseUtil.getMsg();
                if (code == 401) { // 后端判断返回 code 为 401 的情况，统一 token 无效
                    tokenUtil.clearToken();
                    Looper.prepare();
                    ToastUtil.another_display(MyApplication.getContext(), msg);
                    Looper.loop();
                    Intent intent = new Intent("LOGIN");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getContext().startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }
}