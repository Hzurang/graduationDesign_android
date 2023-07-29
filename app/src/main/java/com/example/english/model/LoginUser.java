package com.example.english.model;

import java.math.BigInteger;

public class LoginUser {
    private String user_id;

    private String email;

    private String mobile;

    private String ip;

    private String ac_token;

    private String re_token;

    private String eng_level;

    public LoginUser(String user_id, String email, String mobile, String ip, String ac_token, String re_token, String eng_level) {
        this.user_id = user_id;
        this.email = email;
        this.mobile = mobile;
        this.ip = ip;
        this.ac_token = ac_token;
        this.re_token = re_token;
        this.eng_level = eng_level;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAc_token() {
        return ac_token;
    }

    public void setAc_token(String ac_token) {
        this.ac_token = ac_token;
    }

    public String getRe_token() {
        return re_token;
    }

    public void setRe_token(String re_token) {
        this.re_token = re_token;
    }

    public String getEng_level() {
        return eng_level;
    }

    public void setEng_level(String eng_level) {
        this.eng_level = eng_level;
    }

    @Override
    public String toString() {
        return "LoginUser{" +
                "user_id='" + user_id + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", ip='" + ip + '\'' +
                ", ac_token='" + ac_token + '\'' +
                ", re_token='" + re_token + '\'' +
                ", eng_level='" + eng_level + '\'' +
                '}';
    }
}
