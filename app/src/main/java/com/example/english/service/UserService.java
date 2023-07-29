package com.example.english.service;

import com.example.english.dto.ParamLoginCode;
import com.example.english.dto.ParamLoginPwd;
import com.example.english.dto.ParamResetPwd;
import com.example.english.dto.ParamSignUp;
import com.example.english.util.ResponseUtil;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserService {
    @GET("user/sms_code")
    Call<ResponseUtil> getCode(@Query("mobile") String mobile);

    @POST("user/login_mobile")
    Call<ResponseUtil> loginPwd(@Body ParamLoginPwd paramLoginPwd);

    @POST("user/signup_mobile")
    Call<ResponseUtil> signup(@Body ParamSignUp paramSignUp);

    @POST("user/login_sms_code")
    Call<ResponseUtil> loginCode(@Body ParamLoginCode paramLoginCode);

    @PUT("user/neglect/password")
    Call<ResponseUtil> forgetPwd(@Body ParamResetPwd paramResetPwd);

    @GET("user/mine")
    Call<ResponseUtil> getMine();

    @GET("user/check/list")
    Call<ResponseUtil> getCheckList();

    @GET("user/calendar")
    Call<ResponseUtil> getCalendar(@Query("date") int date, @Query("month") int month, @Query("year") int year);

    @GET("user/vocabulary")
    Call<ResponseUtil> getVocabulary();

    @PUT("user/modify/vocabulary")
    Call<ResponseUtil> setVocabularyBook(@Query("eng_level") int eng_level);

    @PUT("user/modify/daily/word")
    Call<ResponseUtil> setDailyWord(@Query("word_daily_num") String word_daily_num);
}
