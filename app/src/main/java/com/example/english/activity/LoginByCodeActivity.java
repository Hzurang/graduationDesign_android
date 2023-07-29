package com.example.english.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.english.MainActivity;
import com.example.english.R;
import com.example.english.constant.Constant;
import com.example.english.dto.ParamLoginCode;
import com.example.english.dto.ParamSignUp;
import com.example.english.model.LoginUser;
import com.example.english.service.UserService;
import com.example.english.util.ActivityCollector;
import com.example.english.util.CodeCountDownUtil;
import com.example.english.util.GsonUtil;
import com.example.english.util.MobileUtil;
import com.example.english.util.ResponseUtil;
import com.example.english.util.ToastUtil;
import com.example.english.util.TokenUtil;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginByCodeActivity extends AppCompatActivity {

    private EditText et_mobile;
    private EditText et_code;
    private Button bt_code;
    private Button bt_login;
    private ImageButton ic_back;

    private String mobile;
    private String code;

    private long totalTimeMillis = 120000; // 倒计时总时间，单位为毫秒

    private final int SUCCESS = 1;

    //retrofit对象
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constant.ROOT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private UserService userService = retrofit.create(UserService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_by_code);
        initViews();
        setListeners();
        ActivityCollector.addActivity(this);
    }

    private void initViews() {
        et_mobile = findViewById(R.id.et10);
        et_code = findViewById(R.id.et11);
        bt_code = findViewById(R.id.button11);
        bt_login = findViewById(R.id.button12);
        ic_back = findViewById(R.id.imageButton7);
    }

    private void setListeners() {
        bt_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = et_mobile.getText().toString();
                if (MobileUtil.isMobile(mobile) == true) {
                    ToastUtil.display(LoginByCodeActivity.this, "手机号不正确");
                    return;
                }
                Call<ResponseUtil> call = userService.getCode(mobile);
                call.enqueue(new Callback<ResponseUtil>() {
                    @Override
                    public void onResponse(Call<ResponseUtil> call, Response<ResponseUtil> response) {
                        ResponseUtil res = response.body();
                        if (res.getCode() == 200) {
                            Message message = new Message();
                            message.what = SUCCESS;
                            handler.sendMessage(message);
                        } else {
                            ToastUtil.display(LoginByCodeActivity.this, res.getMsg());
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseUtil> call, Throwable t) {
                        Logger.e("Register", "onFailure: 网络连接错误");
                        t.printStackTrace();
                    }
                });
            }
        });

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = et_mobile.getText().toString();
                code = et_code.getText().toString();
                if (MobileUtil.isMobile(mobile) == true) {
                    ToastUtil.display(LoginByCodeActivity.this, "手机号不正确");
                    return;
                }
                ParamLoginCode paramLoginCode = new ParamLoginCode();
                paramLoginCode.setMobile(mobile);
                paramLoginCode.setCode(code);
                Call<ResponseUtil> call = userService.loginCode(paramLoginCode);
                call.enqueue(new Callback<ResponseUtil>() {
                    @Override
                    public void onResponse(Call<ResponseUtil> call, Response<ResponseUtil> response) {
                        ResponseUtil res = response.body();
                        Object data = res.getData();
                        String userJson = new String();
                        if (data != null) {
                            userJson = GsonUtil.object2Json(data);
                        }
                        LoginUser loginUser = new Gson().fromJson(userJson, LoginUser.class);
                        if (res.getCode() == 200) {
                            CodeCountDownUtil.reset();
                            bt_code.setEnabled(true);
                            bt_code.setText("获取验证码");
                            String ac_token = loginUser.getAc_token();
                            TokenUtil tokenUtil = new TokenUtil(LoginByCodeActivity.this);
                            tokenUtil.saveToken(ac_token);
                            Intent intent = new Intent();
                            if (loginUser.getEng_level().equals("0")) {
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setClass(LoginByCodeActivity.this, ChooseWordDBActivity.class);
                                startActivity(intent);
                            } else {
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setClass(LoginByCodeActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            ToastUtil.display(LoginByCodeActivity.this, res.getMsg());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseUtil> call, Throwable t) {
                        Logger.e("Login_by_code", "onFailure: 网络连接错误");
                        t.printStackTrace();
                    }
                });
            }
        });

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(LoginByCodeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    ToastUtil.display(LoginByCodeActivity.this, "验证码已发送");
                    bt_code.setEnabled(false); // 禁用按钮

                    new CountDownTimer(totalTimeMillis, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            long seconds = millisUntilFinished / 1000;
                            String countdownText = seconds + " 秒后重新获取";
                            bt_code.setText(countdownText);
                        }

                        @Override
                        public void onFinish() {
                            bt_code.setEnabled(true); // 恢复按钮为可用状态
                            bt_code.setText("获取验证码"); // 恢复按钮文本为原始获取验证码文本
                        }
                    }.start();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CodeCountDownUtil.cancel();
        ActivityCollector.removeActivity(this);
    }
}

