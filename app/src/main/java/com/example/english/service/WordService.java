package com.example.english.service;

import com.example.english.util.ResponseUtil;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WordService {
    @GET("word/list")
    Call<ResponseUtil> getWordListByType(@Query("word_type") String word_type);
}
