package com.example.english;

import static com.example.english.util.StringUtils.isEmpty;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.example.english.activity.LoginActivity;
import com.example.english.activity.RegisterActivity;
import com.example.english.ui.essay.EssayFragment;
import com.example.english.ui.essay.EssayViewModel;
import com.example.english.util.ActivityCollector;
import com.example.english.util.ToastUtil;
import com.example.english.util.TokenUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;



public class MainActivity extends AppCompatActivity implements EssayFragment.OnScrollListener {



    private EssayViewModel essayViewModel;

    private BottomNavigationView navView;

    public static boolean needRefresh = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_word, R.id.navigation_listen, R.id.navigation_essay, R.id.navigation_mine)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(navView, navController);

        TokenUtil tokenUtil = new TokenUtil(MainActivity.this);
        String ac_token = tokenUtil.getToken();
        System.out.println(ac_token);
        if (isEmpty(ac_token) == true) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClass(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    // 实现接口方法，隐藏底部导航栏
    @Override
    public void onScrollUp() {
        navView.setVisibility(View.GONE);
    }

    // 实现接口方法，显示底部导航栏
    @Override
    public void onScrollDown() {
        navView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("提示")
                .setMessage("今天不再学习了吗？")
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        needRefresh = true;
                        ActivityCollector.finishAll();
                        finishAffinity();
                    }
                })
                .setNegativeButton("再看看", null)
                .show();
    }

    public EssayViewModel getEssayViewModel() {
        return essayViewModel;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}