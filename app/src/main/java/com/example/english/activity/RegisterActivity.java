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
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 接受条例未实现
 */
public class RegisterActivity extends AppCompatActivity {
    //返回登录界面的按钮
    private ImageButton ic_back;
    //注册按钮
    private Button bt_register;
    //获取验证码的按钮
    private Button bt_code;

    private EditText et_mobile;
    private EditText et_pwd;
    private EditText et_pwd2;
    private EditText et_code;
    private EditText et_invite;

    //注册信息：手机号，验证码，第一次输入的密码，第二次输入的确认密码
    private String mobile;
    private String code;
    private String loginPwd;
    private String confirmPwd;
    private String invite_code;

    final int IC_BACK = R.id.imageButton;
    final int BT_REGISTER = R.id.button6;
    final int BT_CODE = R.id.button7;

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
        setContentView(R.layout.register);
        initViews();
        setListeners();
        ActivityCollector.addActivity(this);
    }

    private void initViews() {
        ic_back = findViewById(R.id.imageButton);
        bt_register = findViewById(R.id.button6);
        bt_code = findViewById(R.id.button7);
        et_mobile = findViewById(R.id.et3);
        et_pwd = findViewById(R.id.et4);
        et_code = findViewById(R.id.et5);
        et_pwd2 = findViewById(R.id.et6);
        et_invite = findViewById(R.id.et7);
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        ic_back.setOnClickListener(onClick);
        bt_register.setOnClickListener(onClick);
        bt_code.setOnClickListener(onClick);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    ToastUtil.display(RegisterActivity.this, "验证码已发送");
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

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            switch (viewId) {
                case IC_BACK:
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClass(RegisterActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                    break;
                case BT_REGISTER:
                    mobile = et_mobile.getText().toString();
                    invite_code = et_invite.getText().toString();
                    code = et_code.getText().toString();
                    loginPwd = et_pwd.getText().toString();
                    confirmPwd = et_pwd2.getText().toString();
                    if (MobileUtil.isMobile(mobile) == true) {
                        ToastUtil.display(RegisterActivity.this, "手机号不正确");
                        break;
                    }
                    if (!loginPwd.equals(confirmPwd)) {
                        ToastUtil.display(RegisterActivity.this, "两次输入的密码不一致，请重新输入");
                        break;
                    }
                    if (isEmpty(loginPwd) || isEmpty(confirmPwd)) {
                        ToastUtil.display(RegisterActivity.this, "密码字段不能为空");
                        break;
                    }
                    ParamSignUp paramSignUp = new ParamSignUp();
                    paramSignUp.setMobile(mobile);
                    paramSignUp.setPassword(loginPwd);
                    paramSignUp.setRe_password(confirmPwd);
                    paramSignUp.setCode(code);
                    if (!isEmpty(invite_code)) {
                        paramSignUp.setInvitation_code(invite_code);
                    }
                    Call<ResponseUtil> call = userService.signup(paramSignUp);
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
                                TokenUtil tokenUtil = new TokenUtil(RegisterActivity.this);
                                tokenUtil.saveToken(ac_token);
                                ToastUtil.display(RegisterActivity.this, "注册成功！\n即将开始学习之旅！");
                                Intent intent = new Intent();
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setClass(RegisterActivity.this, ChooseWordDBActivity.class);
                                startActivity(intent);
                            } else {
                                ToastUtil.display(RegisterActivity.this, res.getMsg());
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseUtil> call, Throwable t) {
                            Logger.e("Register", "onFailure: 网络连接错误");
                            t.printStackTrace();
                        }
                    });
                    break;
                case BT_CODE:
                    mobile = et_mobile.getText().toString();
                    Call<ResponseUtil> call_1 = userService.getCode(mobile);
                    call_1.enqueue(new Callback<ResponseUtil>() {
                        @Override
                        public void onResponse(Call<ResponseUtil> call_1, Response<ResponseUtil> response) {
                            ResponseUtil res = response.body();
                            if (res.getCode() == 200) {
                                Message message = new Message();
                                message.what = SUCCESS;
                                handler.sendMessage(message);
                            } else {
                                ToastUtil.display(RegisterActivity.this, res.getMsg());
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseUtil> call_1, Throwable t) {
                            Logger.e("Register", "onFailure: 网络连接错误");
                            t.printStackTrace();
                        }
                    });
            }
        }
    }
}
