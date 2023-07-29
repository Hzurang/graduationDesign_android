package com.example.english.util;


public class ValueUtil {

    public static Boolean isEmpty(String str){
        if(str == null || str.length()==0)
            return true;
        return false;
    }
}