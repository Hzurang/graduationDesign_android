package com.example.english.dto;

import com.google.gson.annotations.SerializedName;

public class ContentApiResponse {
    @SerializedName("content")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
