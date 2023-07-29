package com.example.english.activity;

import static com.example.english.util.StringUtils.isEmpty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.english.MainActivity;
import com.example.english.R;
import com.example.english.constant.Constant;
import com.example.english.dto.ParamLoginPwd;
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

public class LoginActivity extends AppCompatActivity {
    private EditText et_mobile;
    private EditText et_pwd;
    private Button btn_login;
    private Button btn_toRegister;

    private Button btn_forget;

    private Button btn_login_by_code;

    private String mobile;
    private String pwd;

    final int BTN_LOGIN = R.id.button_login;
    final int BTN_TO_REGISTER = R.id.button_toregister;
    final int BTN_FORGET = R.id.button_forget;
    final int BTN_LOGIN_BY_CODE = R.id.button_code;

    //retrofit对象
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constant.ROOT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private UserService userService = retrofit.create(UserService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Intent intent = getIntent();
        initViews();
        setListeners();
        ActivityCollector.addActivity(this);
    }


    private void initViews() {
        et_mobile = findViewById(R.id.et_mobile);
        et_pwd = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.button_login);
        btn_forget = findViewById(R.id.button_forget);
        btn_toRegister = findViewById(R.id.button_toregister);
        btn_login_by_code = findViewById(R.id.button_code);
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        btn_login.setOnClickListener(onClick);
        btn_toRegister.setOnClickListener(onClick);
        btn_login_by_code.setOnClickListener(onClick);
        btn_forget.setOnClickListener(onClick);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            System.out.println(viewId);
            switch (viewId) {
                case BTN_LOGIN:
                    mobile = et_mobile.getText().toString();
                    pwd = et_pwd.getText().toString();
                    if (MobileUtil.isMobile(mobile) == true) {
                        ToastUtil.display(LoginActivity.this, "手机号不正确");
                        break;
                    } else if (isEmpty(pwd)) {
                        ToastUtil.display(LoginActivity.this, "密码不能为空");
                        break;
                    }
                    ParamLoginPwd paramLoginPwd = new ParamLoginPwd();
                    paramLoginPwd.setMobile(mobile);
                    paramLoginPwd.setPassword(pwd);
                    Call<ResponseUtil> call = userService.loginPwd(paramLoginPwd);
                    call.enqueue(new Callback<ResponseUtil>() {
                        @Override
                        public void onResponse(Call<ResponseUtil> call, Response<ResponseUtil> response) {
                            System.out.println("response:-->" + response);
                            ResponseUtil res = response.body();
                            Object data = res.getData();
                            String userJson = new String();
                            if (data != null) {
                                userJson = GsonUtil.object2Json(data);
                            }
                            LoginUser loginUser = new Gson().fromJson(userJson, LoginUser.class);
                            System.out.println("res:-->" + res);
                            if (res.getCode() == 200) {
                                String ac_token = loginUser.getAc_token();
                                TokenUtil tokenUtil = new TokenUtil(LoginActivity.this);
                                tokenUtil.saveToken(ac_token);
                                ToastUtil.display(LoginActivity.this, "登录成功");
                                if (loginUser.getEng_level().equals("0")) {
                                    Intent intent = new Intent();
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setClass(LoginActivity.this, ChooseWordDBActivity.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent();
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setClass(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            } else {
                                ToastUtil.display(LoginActivity.this, res.getMsg());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseUtil> call, Throwable t) {
                            Logger.e("Login", "onFailure: 网络连接错误");
                            t.printStackTrace();
                        }
                    });
                    break;
                case BTN_TO_REGISTER:
                    Intent intent3 = new Intent();
                    intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent3.setClass(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent3);
                    break;
                case BTN_LOGIN_BY_CODE:
                    Intent intent1 = new Intent();
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent1.setClass(LoginActivity.this,
                            LoginByCodeActivity.class);
                    startActivity(intent1);
                    break;
                case BTN_FORGET:
                    Intent intent2 = new Intent();
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent2.setClass(LoginActivity.this,
                            ForgetPwdActivity.class);
                    startActivity(intent2);
                    break;
            }
        }
    }
}
