package com.example.english.util;

import android.content.Context;

import com.example.english.constant.Constant;
import com.example.english.interceptor.HeaderInterceptor;
import com.example.english.interceptor.TokenInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRequest {

    public static Retrofit getInstance(Context context){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new TokenInterceptor(new TokenUtil(context)))
                .addInterceptor(new HeaderInterceptor(new TokenUtil(context)))
                .build();
        return new Retrofit.Builder()
                .baseUrl(Constant.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }
}
