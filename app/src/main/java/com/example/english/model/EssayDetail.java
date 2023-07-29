package com.example.english.model;

import com.google.gson.annotations.SerializedName;

public class EssayDetail {
    @SerializedName("essay_id")
    private String essay_id;

    @SerializedName("essay_title")
    private String essay_title;

    @SerializedName("essay_author")
    private String essay_author;

    @SerializedName("publish_at")
    private String publish_at;

    @SerializedName("essay_content")
    private String essay_content;

    @SerializedName("essay_type")
    private int essay_type;

    @SerializedName("is_collect")
    private int is_collect;

    public EssayDetail(String essay_id, String essay_title, String essay_author, String publish_at, String essay_content, int essay_type, int is_collect) {
        this.essay_id = essay_id;
        this.essay_title = essay_title;
        this.essay_author = essay_author;
        this.publish_at = publish_at;
        this.essay_content = essay_content;
        this.essay_type = essay_type;
        this.is_collect = is_collect;
    }

    public int getEssay_type() {
        return essay_type;
    }

    public void setEssay_type(int essay_type) {
        this.essay_type = essay_type;
    }

    public String getEssay_id() {
        return essay_id;
    }

    public void setEssay_id(String essay_id) {
        this.essay_id = essay_id;
    }

    public String getEssay_title() {
        return essay_title;
    }

    public void setEssay_title(String essay_title) {
        this.essay_title = essay_title;
    }

    public String getEssay_author() {
        return essay_author;
    }

    public void setEssay_author(String essay_author) {
        this.essay_author = essay_author;
    }

    public String getPublish_at() {
        return publish_at;
    }

    public void setPublish_at(String publish_at) {
        this.publish_at = publish_at;
    }

    public String getEssay_content() {
        return essay_content;
    }

    public void setEssay_content(String essay_content) {
        this.essay_content = essay_content;
    }

    public int getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(int is_collect) {
        this.is_collect = is_collect;
    }

    @Override
    public String toString() {
        return "EssayDetail{" +
                "essay_id='" + essay_id + '\'' +
                ", essay_title='" + essay_title + '\'' +
                ", essay_author='" + essay_author + '\'' +
                ", publish_at='" + publish_at + '\'' +
                ", essay_content='" + essay_content + '\'' +
                ", essay_type=" + essay_type +
                ", is_collect=" + is_collect +
                '}';
    }
}
