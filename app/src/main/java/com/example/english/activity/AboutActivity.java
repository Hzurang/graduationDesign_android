package com.example.english.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.english.R;
import com.example.english.config.ConstantData;
import com.example.english.util.ActivityCollector;
import com.example.english.util.MyApplication;
import com.example.english.util.NumberUtil;


public class AboutActivity extends AppCompatActivity {

    private ImageView ic_back;
    private TextView textVersion, textName, textContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        init();
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        textVersion.setText("当前版本：" + getAppVersionName(AboutActivity.this) + "（" + getAppVersionCode(AboutActivity.this) + "）");
        textContent.setText(ConstantData.phrases[NumberUtil.getRandomNumber(0, ConstantData.phrases.length - 1)]);
        textName.setText(getAppName(MyApplication.getContext()));
        ActivityCollector.addActivity(this);
    }

    private void init() {
        ic_back = findViewById(R.id.ic_back);
        textVersion = findViewById(R.id.text_about_version);
        textName = findViewById(R.id.text_about_name);
        textContent = findViewById(R.id.text_about_content);
    }

    public static String getAppVersionCode(Context context) {
        int versioncode = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            // versionName = pi.versionName;
            versioncode = pi.versionCode;
        } catch (Exception e) {
            //Log.e("VersionInfo", "Exception", e);
        }
        return versioncode + "";
    }

    public static String getAppVersionName(Context context) {
        String versionName = null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            //Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    public static String getAppName(Context context) {
        if (context == null) {
            return null;
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            return String.valueOf(packageManager.getApplicationLabel(context.getApplicationInfo()));
        } catch (Throwable e) {
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void onBackPressed() {
        // 点击返回按钮返回到Fragment
        super.onBackPressed();
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

}
