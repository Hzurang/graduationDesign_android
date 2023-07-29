package com.example.english.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    public static void display(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public static void another_display(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
