package com.example.english.model;

import com.google.gson.annotations.SerializedName;

public class Date {
    @SerializedName("year")
    private int year;

    @SerializedName("month")
    private int month;

    @SerializedName("date")
    private int date;

    @SerializedName("word_learn_number")
    private int word_learn_number;

    @SerializedName("word_review_number")
    private int word_review_number;

    @SerializedName("remark")
    private String remark;

    public Date(int year, int month, int date, int word_learn_number, int word_review_number, String remark) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.word_learn_number = word_learn_number;
        this.word_review_number = word_review_number;
        this.remark = remark;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getWord_learn_number() {
        return word_learn_number;
    }

    public void setWord_learn_number(int word_learn_number) {
        this.word_learn_number = word_learn_number;
    }

    public int getWord_review_number() {
        return word_review_number;
    }

    public void setWord_review_number(int word_review_number) {
        this.word_review_number = word_review_number;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Date{" +
                "year=" + year +
                ", month=" + month +
                ", date=" + date +
                ", word_learn_number=" + word_learn_number +
                ", word_review_number=" + word_review_number +
                ", remark='" + remark + '\'' +
                '}';
    }
}
