package com.example.english.dto;

import com.google.gson.annotations.SerializedName;

public class ParamLoginCode {
    @SerializedName("mobile")
    private String mobile;

    @SerializedName("code")
    private String code;

    public ParamLoginCode(String mobile, String code) {
        this.mobile = mobile;
        this.code = code;
    }

    public ParamLoginCode() {

    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ParamLoginCode{" +
                "mobile='" + mobile + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
