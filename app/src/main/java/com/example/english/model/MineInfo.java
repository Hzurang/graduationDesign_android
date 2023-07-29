package com.example.english.model;

import com.google.gson.annotations.SerializedName;

public class MineInfo {
    @SerializedName("nickname")
    private String nickname;

    @SerializedName("integral")
    private String integral;

    @SerializedName("days")
    private String days;

    @SerializedName("words")
    private String words;

    public MineInfo(String nickname, String integral, String days, String words) {
        this.nickname = nickname;
        this.integral = integral;
        this.days = days;
        this.words = words;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    @Override
    public String toString() {
        return "MineInfo{" +
                "nickname='" + nickname + '\'' +
                ", integral='" + integral + '\'' +
                ", days='" + days + '\'' +
                ", words='" + words + '\'' +
                '}';
    }
}
