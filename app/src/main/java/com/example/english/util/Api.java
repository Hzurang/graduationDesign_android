package com.example.english.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Api {
    public static String parseImageUrl(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            JSONArray imagesArray = jsonObject.getJSONArray("images");
            if (imagesArray.length() > 0) {
                JSONObject imageObject = imagesArray.getJSONObject(0);
                String imageUrl = "https://www.bing.com" + imageObject.getString("url");
                return imageUrl;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
