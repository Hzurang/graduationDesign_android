package com.example.english.model;

import com.google.gson.annotations.SerializedName;

public class Vocabulary {
    @SerializedName("word_need_recite_num")
    private int word_need_recite_num;

    @SerializedName("eng_level")
    private int eng_level;

    public Vocabulary(int word_need_recite_num, int eng_level) {
        this.word_need_recite_num = word_need_recite_num;
        this.eng_level = eng_level;
    }

    public int getWord_need_recite_num() {
        return word_need_recite_num;
    }

    public void setWord_need_recite_num(int word_need_recite_num) {
        this.word_need_recite_num = word_need_recite_num;
    }

    public int getEng_level() {
        return eng_level;
    }

    public void setEng_level(int eng_level) {
        this.eng_level = eng_level;
    }

    @Override
    public String toString() {
        return "Vocabulary{" +
                "word_need_recite_num=" + word_need_recite_num +
                ", eng_level=" + eng_level +
                '}';
    }
}
