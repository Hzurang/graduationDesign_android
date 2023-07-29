package com.example.english.model;

import com.google.gson.annotations.SerializedName;

public class Essay {
    @SerializedName("essay_title")
    private String title;

    @SerializedName("publish_at")
    private String publishAt;

    @SerializedName("essay_collect_num")
    private String collect_num;

    @SerializedName("essay_author")
    private String author;

    @SerializedName("essay_id")
    private String essay_id;

    public Essay(String title, String publishAt, String collect_num, String author, String essay_id) {
        this.title = title;
        this.publishAt = publishAt;
        this.collect_num = collect_num;
        this.author = author;
        this.essay_id = essay_id;
    }

    public String getEssay_id() {
        return essay_id;
    }

    public void setEssay_id(String essay_id) {
        this.essay_id = essay_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(String publishAt) {
        this.publishAt = publishAt;
    }

    public String getCollect_num() {
        return collect_num;
    }

    public void setCollect_num(String collect_num) {
        this.collect_num = collect_num;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Essay{" +
                "title='" + title + '\'' +
                ", publishAt='" + publishAt + '\'' +
                ", collect_num='" + collect_num + '\'' +
                ", author='" + author + '\'' +
                ", essay_id='" + essay_id + '\'' +
                '}';
    }
}
