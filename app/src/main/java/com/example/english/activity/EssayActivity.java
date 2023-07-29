package com.example.english.activity;

import android.app.Activity;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.english.R;
import com.example.english.model.EssayDetail;
import com.example.english.service.EssayService;
import com.example.english.util.ActivityCollector;
import com.example.english.util.GsonUtil;
import com.example.english.util.ResponseUtil;
import com.example.english.util.RetrofitRequest;
import com.example.english.util.ToastUtil;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EssayActivity extends AppCompatActivity {
    private ImageButton bt_back;
    private Button bt_collect;
    private TextView tv_title;
    private TextView tv_author;
    private TextView tv_date;
    private TextView tv_type;
    private TextView tv_content;
    private String essay_id;

    //retrofit对象
    private Retrofit retrofit;

    private EssayService essayService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.essay_detail);
        retrofit = RetrofitRequest.getInstance(EssayActivity.this);
        essayService = retrofit.create(EssayService.class);
        essay_id = getIntent().getStringExtra("essay_id");
        initViews();
        setListeners();
        loadData();
        ActivityCollector.addActivity(this);
    }

    private void loadData() {
        Call<ResponseUtil> call = essayService.getEssayDetail(essay_id);
        call.enqueue(new Callback<ResponseUtil>() {
            @Override
            public void onResponse(Call<ResponseUtil> call, Response<ResponseUtil> response) {
                ResponseUtil res = response.body();
                Object data = res.getData();
                String essayJson = new String();
                if (data != null) {
                    essayJson = GsonUtil.object2Json(data);
                }
                EssayDetail essayDetail = new Gson().fromJson(essayJson, EssayDetail.class);
                if (res.getCode() == 200) {
                    tv_title.setText(essayDetail.getEssay_title());
                    tv_author.setText("作者："+essayDetail.getEssay_author());
                    tv_date.setText("发布时间："+essayDetail.getPublish_at());
                    tv_content.setText(essayDetail.getEssay_content());
                    switch (essayDetail.getEssay_type()) {
                        case 1:
                            tv_type.setText("文章类型：英文小说");
                            break;
                        case 2:
                            tv_type.setText("文章类型：情感故事");
                            break;
                        case 3:
                            tv_type.setText("文章类型：英语美文");
                            break;
                        case 4:
                            tv_type.setText("文章类型：双语故事");
                            break;
                    }
                    switch (essayDetail.getIs_collect()) {
                        case 1:
                            bt_collect.setText("已收藏");
                            break;
                        case 0:
                            bt_collect.setText("未收藏");
                            break;
                    }
                } else {
                    ToastUtil.display(EssayActivity.this, res.getMsg());
                }
            }

            @Override
            public void onFailure(Call<ResponseUtil> call, Throwable t) {
                Logger.e("EssayDetail", "onFailure: 网络连接错误");
                t.printStackTrace();
            }
        });
    }

    private void initViews() {
        bt_back = findViewById(R.id.imageButton1);
        bt_collect = findViewById(R.id.button_collect);
        tv_title = findViewById(R.id.text_title);
        tv_author = findViewById(R.id.text_author);
        tv_date = findViewById(R.id.text_date);
        tv_type = findViewById(R.id.text_type);
        tv_content = findViewById(R.id.text_content);
    }

    private void setListeners() {
        bt_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = bt_collect.getText().toString();
                if (text.equals("未收藏")) {
                    Call<ResponseUtil> call = essayService.collectEssay(essay_id);
                    call.enqueue(new Callback<ResponseUtil>() {
                        @Override
                        public void onResponse(Call<ResponseUtil> call, Response<ResponseUtil> response) {
                            ResponseUtil res = response.body();
                            if (res.getCode() == 200) {
                                ToastUtil.display(EssayActivity.this, res.getMsg());
                                bt_collect.setText("已收藏");
                            } else {
                                ToastUtil.display(EssayActivity.this, res.getMsg());
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseUtil> call, Throwable t) {
                            Logger.e("EssayDetail", "onFailure: 网络连接错误");
                            t.printStackTrace();
                        }
                    });
                } else if (text.equals("已收藏")) {
                    Call<ResponseUtil> call = essayService.cancelCollectEssay(essay_id);
                    call.enqueue(new Callback<ResponseUtil>() {
                        @Override
                        public void onResponse(Call<ResponseUtil> call, Response<ResponseUtil> response) {
                            ResponseUtil res = response.body();
                            if (res.getCode() == 200) {
                                ToastUtil.display(EssayActivity.this, res.getMsg());
                                bt_collect.setText("未收藏");
                            } else {
                                ToastUtil.display(EssayActivity.this, res.getMsg());
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseUtil> call, Throwable t) {
                            Logger.e("EssayDetail", "onFailure: 网络连接错误");
                            t.printStackTrace();
                        }
                    });
                }
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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

    private String getResourceName(Drawable drawable) {
        if (drawable != null) {
            int resourceId = getResources().getIdentifier(drawable.toString(), null, null);
            if (resourceId != 0) {
                return getResources().getResourceEntryName(resourceId);
            }
        }
        return "";
    }
}