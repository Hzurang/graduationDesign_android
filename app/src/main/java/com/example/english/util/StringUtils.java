package com.example.english.util;

public class StringUtils {
    /**
     * 判断是否为空值
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return false;
    }
}