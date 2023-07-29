package com.example.english.dto;

import com.google.gson.annotations.SerializedName;

public class ParamResetPwd {
    @SerializedName("mobile")
    private String mobile;

    @SerializedName("code")
    private String code;

    @SerializedName("password")
    private String password;

    @SerializedName("re_password")
    private String re_password;

    public ParamResetPwd(String mobile, String code, String password, String re_password) {
        this.mobile = mobile;
        this.code = code;
        this.password = password;
        this.re_password = re_password;
    }

    public ParamResetPwd() {

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRe_password() {
        return re_password;
    }

    public void setRe_password(String re_password) {
        this.re_password = re_password;
    }

    @Override
    public String toString() {
        return "ParamResetPwd{" +
                "mobile='" + mobile + '\'' +
                ", code='" + code + '\'' +
                ", password='" + password + '\'' +
                ", re_password='" + re_password + '\'' +
                '}';
    }
}
