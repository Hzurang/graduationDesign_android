package com.example.english.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.english.R;
import com.example.english.config.ConfigData;
import com.example.english.config.ConstantData;
import com.example.english.model.Date;
import com.example.english.model.LoginUser;
import com.example.english.model.Vocabulary;
import com.example.english.service.UserService;
import com.example.english.util.GsonUtil;
import com.example.english.util.ResponseUtil;
import com.example.english.util.RetrofitRequest;
import com.example.english.util.TimeUtil;
import com.example.english.util.ToastUtil;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PlanActivity extends AppCompatActivity {

    private TextView textBookName, textNum, textDaily, textExpect, textInfor;

    private ImageView imgBook;

    private RelativeLayout layoutChange, layoutData;

    private String[] planStyle = {"修改书本", "修改每日学习单词量", "重置单词书"};

    private EditText editSpeed, editMatch;

    private int wordNum;
    private Retrofit retrofit;
    private UserService userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        retrofit = RetrofitRequest.getInstance(PlanActivity.this);
        System.out.println(retrofit);
        userService = retrofit.create(UserService.class);
        init();
        updateData();

        Call<ResponseUtil> call = userService.getVocabulary();
        call.enqueue(new Callback<ResponseUtil>() {
            @Override
            public void onResponse(Call<ResponseUtil> call, Response<ResponseUtil> response) {
                ResponseUtil res = response.body();
                Object data = res.getData();
                String vocabularyJson = new String();
                if (data != null) {
                    vocabularyJson = GsonUtil.object2Json(data);
                }
                Vocabulary vocabulary = new Gson().fromJson(vocabularyJson, Vocabulary.class);
                if (res.getCode() == 200) {
                    int bookId = vocabulary.getEng_level();
                    wordNum = ConstantData.wordTotalNumberById(bookId);
                    int dailyNum = vocabulary.getWord_need_recite_num();

                    Glide.with(PlanActivity.this).load(ConstantData.bookPicById(bookId)).into(imgBook);

                    textNum.setText("词汇量：" + wordNum);
                    textBookName.setText(ConstantData.bookNameById(bookId));
                    textDaily.setText("每日学习单词：" + dailyNum);

                    int days = wordNum / dailyNum + 1;
                    textExpect.setText("预计将于" + TimeUtil.getDayAgoOrAfterString(days) + "初学完所有单词");

                    textInfor.setText("单词数量设置须介于5-" + wordNum + "之间");

                } else {
                    ToastUtil.display(PlanActivity.this, res.getMsg());
                }
            }

            @Override
            public void onFailure(Call<ResponseUtil> call, Throwable t) {
                Logger.e("Com", "onFailure: 网络连接错误");
                t.printStackTrace();
            }
        });

//        layoutChange.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final AlertDialog.Builder builder = new AlertDialog.Builder(PlanActivity.this);
//                builder.setTitle("请选择类别")
//                        .setSingleChoiceItems(planStyle, -1, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(final DialogInterface dialog, final int which) {
//                                // 延迟500毫秒取消对话框
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        dialog.dismiss();
//                                        FragmentWord.prepareData = 0;
//                                        switch (which) {
//                                            case 0:
//                                                ActivityCollector.startOtherActivity(PlanActivity.this, ChooseWordDBActivity.class);
//                                                ConfigData.isReChoose = true;
//                                                break;
//                                            case 1:
//                                                Intent intent = new Intent(PlanActivity.this, ChangePlanActivity.class);
//                                                intent.putExtra(ConfigData.UPDATE_NAME, ConfigData.isUpdate);
//                                                startActivity(intent);
//                                                break;
//                                            case 2:
//                                                dialog.dismiss();
//                                                AlertDialog.Builder builder2 = new AlertDialog.Builder(PlanActivity.this);
//                                                builder2.setTitle("提示")
//                                                        .setMessage("此操作会重置此书的所有学习配置信息，但不会修改释义等信息，且不可逆。（适用于已学完此书，重学一遍）")
//                                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                            @Override
//                                                            public void onClick(DialogInterface dialog, int which) {
//                                                                Word word = new Word();
//                                                                word.setToDefault("isNeedLearned");
//                                                                word.setToDefault("needLearnDate");
//                                                                word.setToDefault("needReviewDate");
//                                                                word.setToDefault("isLearned");
//                                                                word.setToDefault("examNum");
//                                                                word.setToDefault("examRightNum");
//                                                                word.setToDefault("lastMasterTime");
//                                                                word.setToDefault("lastReviewTime");
//                                                                word.setToDefault("masterDegree");
//                                                                word.setToDefault("deepMasterTimes");
//                                                                UserConfig userConfig = new UserConfig();
//                                                                userConfig.setToDefault("lastStartTime");
//                                                                userConfig.updateAll();
//                                                                word.updateAll();
//                                                                Toast.makeText(PlanActivity.this, "重置成功", Toast.LENGTH_SHORT).show();
//                                                                dialog.dismiss();
//                                                            }
//                                                        })
//                                                        .setNegativeButton("取消", null)
//                                                        .show();
//                                                break;
//                                        }
//                                    }
//                                }, 200);
//                            }
//                        }).show();
//            }
//        });

        layoutData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int speedNum = Integer.parseInt(editSpeed.getText().toString());
                int matchNum = Integer.parseInt(editMatch.getText().toString());
                if (speedNum >= 5 && speedNum <= wordNum && matchNum >= 2 && matchNum <= wordNum) {
                    ConfigData.setSpeedNum(speedNum);
                    ConfigData.setMatchNum(matchNum);
                    Toast.makeText(PlanActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    updateData();
                } else {
                    Toast.makeText(PlanActivity.this, "请输入数据在有效范围内", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void init() {
        textBookName = findViewById(R.id.text_plan_name);
        textNum = findViewById(R.id.text_plan_num);
        textDaily = findViewById(R.id.text_plan_daily);
        textExpect = findViewById(R.id.text_plan_expect);
        imgBook = findViewById(R.id.img_plan_book);
        layoutChange = findViewById(R.id.layout_plan_change);
        layoutData = findViewById(R.id.layout_data_change);
        editSpeed = findViewById(R.id.edit_plan_speed);
        editMatch = findViewById(R.id.edit_plan_match);
        textInfor = findViewById(R.id.text_plan_data_info);
    }

    private void updateData() {
        editMatch.setText(ConfigData.getMatchNum() + "");
        editSpeed.setText(ConfigData.getSpeedNum() + "");
    }

}
