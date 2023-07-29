package com.example.english.dto;

import com.google.gson.annotations.SerializedName;

public class ParamLoginPwd {
    @SerializedName("mobile")
    private String mobile;

    @SerializedName("password")
    private String password;

    public ParamLoginPwd(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

    public ParamLoginPwd() {

    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ParamLoginPwd{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
