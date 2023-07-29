package com.example.english.service;

import com.example.english.util.ResponseUtil;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EssayService {
    @GET("essays")
    Call<ResponseUtil> getEssays(@Query("essaytype") String essaytype, @Query("pagenum") String pagenum, @Query("pagesize") String pagesize);

    @GET("essay")
    Call<ResponseUtil> getEssayDetail(@Query("essay_id") String essay_id);

    @POST("essay/collection")
    Call<ResponseUtil> collectEssay(@Query("essay_id") String essay_id);

    @DELETE("essay/cancellation/collection")
    Call<ResponseUtil> cancelCollectEssay(@Query("essay_id") String essay_id);
}
