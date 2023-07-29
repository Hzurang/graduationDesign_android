package com.example.english.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.english.MainActivity;
import com.example.english.R;
import com.example.english.config.ConfigData;
import com.example.english.config.ConstantData;
import com.example.english.constant.Constant;
import com.example.english.dto.WordList;
import com.example.english.model.LoginUser;
import com.example.english.service.EssayService;
import com.example.english.service.UserService;
import com.example.english.service.WordService;
import com.example.english.util.ActivityCollector;
import com.example.english.util.GsonUtil;
import com.example.english.util.MyApplication;
import com.example.english.util.ResponseUtil;
import com.example.english.util.RetrofitRequest;
import com.example.english.util.ToastUtil;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePlanActivity extends AppCompatActivity {

    private EditText editText;

    private TextView textGo, textBook, textWordMaxNum, tips;

    private int maxNum;

    private static final String TAG = "ChangePlanActivity";

    private Thread thread;

    private final int FINISH = 1;
    private final int DOWN_DONE = 2;

    private ProgressDialog progressDialog;

    private Retrofit retrofit;

    private UserService userService;
    private WordService wordService;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case FINISH:
                    // 等待框消失
                    progressDialog.dismiss();
//                    // 重置上次学习时间
//                    UserConfig userConfig1 = new UserConfig();
//                    userConfig1.setLastStartTime(0);
//                    userConfig1.setCurrentBookId(currentBookId);
//                    userConfig1.updateAll("userId = ?", ConfigData.getSinaNumLogged() + "");
//                    // 删除当天打卡记录
//                    Calendar calendar = Calendar.getInstance();
//                    LitePal.deleteAll(MyDate.class, "year = ? and month = ? and date = ? and userId = ?"
//                            , calendar.get(Calendar.YEAR) + ""
//                            , (calendar.get(Calendar.MONTH) + 1) + ""
//                            , calendar.get(Calendar.DAY_OF_MONTH) + ""
//                            , ConfigData.getSinaNumLogged() + "");
                    Intent intent = new Intent();
                    intent.setClass(ChangePlanActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
                case DOWN_DONE:
                    progressDialog.setMessage("已下载完成，正在分析数据中...");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_plan);
        retrofit = RetrofitRequest.getInstance(ChangePlanActivity.this);
        userService = retrofit.create(UserService.class);
        init();

        final Intent intent = getIntent();
        String bookId = intent.getStringExtra("bookId");

        System.out.println("11111111111111            " + bookId);

        try {
            int num = Integer.parseInt(bookId);
            textBook.setText(ConstantData.typeById(num));
            textWordMaxNum.setText(String.valueOf(ConstantData.wordTotalNumberById(num)));
            maxNum = ConstantData.wordTotalNumberById(num);
        } catch (NumberFormatException e) {
            Logger.e("字符串转数字出错");
        }

        textGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().trim().equals("")) {
                    if (Integer.parseInt(editText.getText().toString().trim()) >= 5
                            && Integer.parseInt(editText.getText().toString().trim()) < maxNum) {
                        // 隐藏软键盘
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        // 设置数据
                        // 更新数据

                        Call<ResponseUtil> call = userService.setDailyWord(editText.getText().toString());
                        call.enqueue(new Callback<ResponseUtil>() {
                            @Override
                            public void onResponse(Call<ResponseUtil> call, retrofit2.Response<ResponseUtil> response) {
                                ResponseUtil res = response.body();
                                if (res.getCode() == 200) {
                                    // 是第一次设置数据
                                    if (ConfigData.notUpdate == intent.getIntExtra(ConfigData.UPDATE_NAME, 0)) {
                                        // 开启等待框
                                        progressDialog = new ProgressDialog(ChangePlanActivity.this);
                                        progressDialog.setTitle("请稍等");
                                        progressDialog.setMessage("数据包正在下载中...");
                                        progressDialog.setCancelable(false);
                                        progressDialog.show();
                                        // 延迟两秒再运行，防止等待框不显示
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                // 开启线程分析数据
                                                thread = new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Call<ResponseUtil> call1 = wordService.getWordListByType(bookId);
                                                        call1.enqueue(new Callback<ResponseUtil>() {
                                                            @Override
                                                            public void onResponse(Call<ResponseUtil> call, Response<ResponseUtil> response) {
                                                                ResponseUtil res = response.body();
                                                                Object data = res.getData();
                                                                String stringJson = new String();
                                                                if (data != null) {
                                                                    stringJson = GsonUtil.object2Json(data);
                                                                }
                                                                WordList wordList = new Gson().fromJson(stringJson, WordList.class);
                                                                if (res.getCode() == 200) {
                                                                    Message message = new Message();
                                                                    message.what = DOWN_DONE;
                                                                    handler.sendMessage(message);
                                                                    System.out.println(wordList.getData());

                                                                }
                                                            }
                                                            @Override
                                                            public void onFailure(Call<ResponseUtil> call, Throwable t) {
                                                                Logger.e("setVocabularyBook", "onFailure: 网络连接错误");
                                                                t.printStackTrace();
                                                            }
                                                        });
                                                        Message message = new Message();
                                                        message.what = FINISH;
                                                        handler.sendMessage(message);
                                                    }
                                                });
                                                thread.start();
                                            }
                                        }, 500);
                                    }
                                } else {
                                    ToastUtil.display(MyApplication.getContext(), res.getMsg());
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseUtil> call, Throwable t) {
                                Logger.e("setVocabularyBook", "onFailure: 网络连接错误");
                                t.printStackTrace();
                            }
                        });

//                        } else {
//                            if (userConfigs.get(0).getWordNeedReciteNum() != Integer.parseInt(editText.getText().toString().trim())) {
//                                // 重置上次学习时间
//                                UserConfig userConfig1 = new UserConfig();
//                                userConfig1.setLastStartTime(-1);
//                                userConfig1.updateAll("userId = ?", ConfigData.getSinaNumLogged() + "");
//                                Toast.makeText(ChangePlanActivity.this, "" + LitePal.where("userId = ?", ConfigData.getSinaNumLogged() + "").find(UserConfig.class).get(0).getLastStartTime(), Toast.LENGTH_SHORT).show();
//                                Log.d(TAG, "onClick: " + LitePal.where("userId = ?", ConfigData.getSinaNumLogged() + "").find(UserConfig.class).get(0).getLastStartTime());
//                                // 删除当天打卡记录
//                                Calendar calendar = Calendar.getInstance();
//                                LitePal.deleteAll(MyDate.class, "year = ? and month = ? and date = ? and userId = ?"
//                                        , calendar.get(Calendar.YEAR) + ""
//                                        , (calendar.get(Calendar.MONTH) + 1) + ""
//                                        , calendar.get(Calendar.DAY_OF_MONTH) + ""
//                                        , ConfigData.getSinaNumLogged() + "");
//                                Toast.makeText(ChangePlanActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(ChangePlanActivity.this, "计划未变", Toast.LENGTH_SHORT).show();
//                            }
//                            ActivityCollector.startOtherActivity(ChangePlanActivity.this, MainActivity.class);
//                        }
                        } else {
                            Toast.makeText(ChangePlanActivity.this, "请输入合理的范围", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ChangePlanActivity.this, "请输入值再继续", Toast.LENGTH_SHORT).show();
                    }
                }
        });
    }

    private void init() {
        editText = findViewById(R.id.edit_word_num);
        textGo = findViewById(R.id.text_plan_next);
        textBook = findViewById(R.id.text_plan_chosen);
        textWordMaxNum = findViewById(R.id.text_max_word_num);
        tips = findViewById(R.id.tips);
        tips.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Log.d(TAG, "onStart: ");
//        // 获得数据
//        List<UserConfig> userConfigs = LitePal.where("userId = ?", ConfigData.getSinaNumLogged() + "").find(UserConfig.class);
//        currentBookId = userConfigs.get(0).getCurrentBookId();
//
//        maxNum = ConstantData.wordTotalNumberById(currentBookId);
//
//        // 设置最大背单词量
//        textWordMaxNum.setText(maxNum + "");
//
//        // 设置书名
//        textBook.setText(ConstantData.bookNameById(currentBookId));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(ChangePlanActivity.this, ChooseWordDBActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
