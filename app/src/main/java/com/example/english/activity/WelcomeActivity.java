package com.example.english.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import androidx.appcompat.app.AppCompatActivity;
import com.example.english.MainActivity;
import com.example.english.R;
import com.example.english.util.ActivityCollector;
import com.example.english.util.CodeCountDownUtil;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(3000);// 在欢迎界面停留2.0秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = hand.obtainMessage();
                hand.sendMessage(msg);
            }

        }.start();
        ActivityCollector.addActivity(this);
    }

    Handler hand = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (isFirstRun()) {
                // 如果是第一次启动程序则进入引导界面
                Intent intent = new Intent(WelcomeActivity.this,
                        GuideActivity.class);
                startActivity(intent);
            } else {
                // 如果不是第一次启动则进入主页
                Intent intent = new Intent(WelcomeActivity.this,
                        MainActivity.class);
                startActivity(intent);
            }
            finish();
        };
    };

    // 判断是否是第一次启动程序 利用 SharedPreferences 将数据保存在本地
    private boolean isFirstRun() {
        //实例化SharedPreferences对象（第一步）
        SharedPreferences sharedPreferences = this.getSharedPreferences(
                "share", MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!isFirstRun) {
            return false;
        } else {
            //保存数据 （第三步）
            editor.putBoolean("isFirstRun", false);
            //提交当前数据 （第四步）
            editor.commit();
            return true;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}

