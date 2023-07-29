package com.example.english.activity;

import static com.example.english.util.StringUtils.isEmpty;

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
import com.example.english.dto.ParamResetPwd;
import com.example.english.dto.ParamSignUp;
import com.example.english.service.UserService;
import com.example.english.util.ActivityCollector;
import com.example.english.util.CodeCountDownUtil;
import com.example.english.util.MobileUtil;
import com.example.english.util.ResponseUtil;
import com.example.english.util.ToastUtil;
import com.orhanobut.logger.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgetPwdActivity extends AppCompatActivity {
    private EditText et_mobile;
    private EditText et_pwd;
    private EditText et_code;
    private EditText et_second_pwd;
    private Button bt_code;
    private Button bt_accept;
    private ImageButton ic_back;

    private String mobile;
    private String reset_Pwd;
    private String code;
    private String second_Pwd;

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
        setContentView(R.layout.forget_pwd);
        initViews();
        setListeners();
        ActivityCollector.addActivity(this);
    }

    private void initViews() {
        et_mobile = findViewById(R.id.et7);
        et_code = findViewById(R.id.et8);
        et_pwd = findViewById(R.id.et9);
        et_second_pwd = findViewById(R.id.et10);
        bt_accept = findViewById(R.id.button9);
        bt_code = findViewById(R.id.button10);
        ic_back = findViewById(R.id.imageButton6);
    }

    private void setListeners() {
        bt_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = et_mobile.getText().toString();
                if (MobileUtil.isMobile(mobile) == true) {
                    ToastUtil.display(ForgetPwdActivity.this, "手机号不正确");
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
                            ToastUtil.display(ForgetPwdActivity.this, res.getMsg());
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

        bt_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = et_mobile.getText().toString();
                code = et_code.getText().toString();
                reset_Pwd = et_pwd.getText().toString();
                second_Pwd = et_second_pwd.getText().toString();
                if (MobileUtil.isMobile(mobile) == true) {
                    ToastUtil.display(ForgetPwdActivity.this, "手机号不正确");
                    return;
                }
                if (!reset_Pwd.equals(second_Pwd)) {
                    ToastUtil.display(ForgetPwdActivity.this, "两次输入的密码不一致，请重新输入");
                    return;
                }
                ParamResetPwd paramResetPwd = new ParamResetPwd();
                paramResetPwd.setMobile(mobile);
                paramResetPwd.setPassword(reset_Pwd);
                paramResetPwd.setRe_password(second_Pwd);
                paramResetPwd.setCode(code);
                Call<ResponseUtil> call = userService.forgetPwd(paramResetPwd);
                call.enqueue(new Callback<ResponseUtil>() {
                    @Override
                    public void onResponse(Call<ResponseUtil> call, Response<ResponseUtil> response) {
                        ResponseUtil res = response.body();
                        if (res.getCode() == 200) {
                            CodeCountDownUtil.reset();
                            bt_code.setEnabled(true);
                            bt_code.setText("获取验证码");
                            ToastUtil.display(ForgetPwdActivity.this, "找回密码成功，请重新登录");
                            Intent intent = new Intent();
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setClass(ForgetPwdActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            ToastUtil.display(ForgetPwdActivity.this, res.getMsg());
                        }

                    }
                    @Override
                    public void onFailure(Call<ResponseUtil> call, Throwable t) {
                        Logger.e("Find_password", "onFailure: 网络连接错误");
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
                intent.setClass(ForgetPwdActivity.this, LoginActivity.class);
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
                    ToastUtil.display(ForgetPwdActivity.this, "验证码已发送");
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
