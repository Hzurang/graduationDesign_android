package com.example.english.util;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenUtil {
    private static final String TOKEN_PREFS = "token_prefs";
    private static final String KEY_TOKEN = "ac_token";

    private SharedPreferences sharedPreferences;

    public TokenUtil(Context context) {
        sharedPreferences = context.getSharedPreferences(TOKEN_PREFS, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public void clearToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_TOKEN);
        editor.apply();
    }
}
