package com.example.english.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.english.R;
import com.example.english.model.Date;
import com.example.english.service.UserService;
import com.example.english.util.ActivityCollector;
import com.example.english.util.GsonUtil;
import com.example.english.util.NormalSpan;
import com.example.english.util.ResponseUtil;
import com.example.english.util.RetrofitRequest;
import com.example.english.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CalendarActivity extends AppCompatActivity {

    private MaterialCalendarView materialCalendarView;

    private TextView textDate, textWord, textRemark, textSign;

    private ImageView im_back;
    private LinearLayout layoutRemark, layoutDate, layoutWord;

    private CardView cardInfor;

    private ImageView imgSign;

    private static final String TAG = "CalendarActivity";

    private Retrofit retrofit;

    private List<Date> dateList;

    private List<Date> dateList2;
    private UserService userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        retrofit = RetrofitRequest.getInstance(CalendarActivity.this);
        userService = retrofit.create(UserService.class);
        init();

        Call<ResponseUtil> call = userService.getCheckList();
        call.enqueue(new Callback<ResponseUtil>() {
            @Override
            public void onResponse(Call<ResponseUtil> call, Response<ResponseUtil> response) {
                ResponseUtil res = response.body();
                if (res.getCode() == 200) {
                    String date = GsonUtil.object2Json(res.getData());
                    dateList = new Gson().fromJson(date, new TypeToken<List<Date>>() {
                    }.getType());
//                    // 在主线程更新UI
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            // 添加新数据到列表
//                            // 刷新适配器
//                            essayAdapter.notifyDataSetChanged();
//                        }
//                    });
                } else {
                    ToastUtil.display(CalendarActivity.this, res.getMsg());
                }
            }

            @Override
            public void onFailure(Call<ResponseUtil> call, Throwable t) {
                Logger.e("Com", "onFailure: 网络连接错误");
                t.printStackTrace();
            }
        });

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDate = calendar.get(Calendar.DAY_OF_MONTH);

        materialCalendarView.setDateSelected(CalendarDay.from(currentYear, currentMonth, currentDate), true);
        materialCalendarView.setWeekDayLabels(new String[]{"SUN", "MON", "TUS", "WED", "THU", "FRI", "SAT"});
        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);

        updateData(CalendarDay.from(currentYear, currentMonth, currentDate));
        if (dateList != null) {
            materialCalendarView.addDecorator(new DayViewDecorator() {
                @Override
                public boolean shouldDecorate(CalendarDay day) {
                    for (Date myDate : dateList) {
                        if (day.getDay() == myDate.getDate() && day.getMonth() == (myDate.getMonth() - 1) && day.getYear() == myDate.getYear())
                            return true;
                    }
                    return false;
                }

                @Override
                public void decorate(DayViewFacade view) {
                    view.addSpan(new NormalSpan());
                }
            });
        }
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                updateData(date);
            }
        });

        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ActivityCollector.addActivity(this);
    }

    private void init() {
        materialCalendarView = findViewById(R.id.calendar);
        textDate = findViewById(R.id.text_cal_date);
        textWord = findViewById(R.id.text_cal_word);
        textRemark = findViewById(R.id.text_cal_remark);
        textSign = findViewById(R.id.text_cal_sign);
        layoutRemark = findViewById(R.id.layout_cal_remark);
        layoutWord = findViewById(R.id.layout_cal_word);
        layoutDate = findViewById(R.id.layout_cal_date);
        layoutRemark = findViewById(R.id.layout_cal_remark);
        cardInfor = findViewById(R.id.card_cal_infor);
        imgSign = findViewById(R.id.img_cal_sign);
        im_back = findViewById(R.id.im_back);
    }

    private void updateData(CalendarDay date) {
        Call<ResponseUtil> call = userService.getCalendar(date.getDay(), (date.getMonth() + 1), date.getYear());
        call.enqueue(new Callback<ResponseUtil>() {
            @Override
            public void onResponse(Call<ResponseUtil> call, Response<ResponseUtil> response) {
                ResponseUtil res = response.body();
                if (res.getCode() == 200) {
                    String date = GsonUtil.object2Json(res.getData());
                    dateList2 = new Gson().fromJson(date, new TypeToken<List<Date>>() {
                    }.getType());
                    if (dateList2.isEmpty()) {
                        layoutDate.setVisibility(View.GONE);
                        layoutRemark.setVisibility(View.GONE);
                        layoutWord.setVisibility(View.GONE);
                        Glide.with(CalendarActivity.this).load(R.drawable.icon_no_done).into(imgSign);
                        textSign.setText("该日学习计划未完成");
                        textSign.setTextColor(getColor(R.color.colorLightBlack));
                        cardInfor.setCardBackgroundColor(getColor(R.color.colorBgWhite));
                    } else {
                        layoutDate.setVisibility(View.VISIBLE);
                        layoutWord.setVisibility(View.VISIBLE);
                        Glide.with(CalendarActivity.this).load(R.drawable.icon_done).into(imgSign);
                        textSign.setText("该日学习计划已完成");
                        textSign.setTextColor(getColor(R.color.colorMainBlue));
                        cardInfor.setCardBackgroundColor(getColor(R.color.colorBgWhite));
                        textDate.setText(dateList2.get(0).getYear() + "年" + dateList2.get(0).getMonth() + "月" + dateList2.get(0).getDate() + "日" + "");
                        textWord.setText((dateList2.get(0).getWord_learn_number() + dateList2.get(0).getWord_review_number()) + "");

                        if (dateList2.get(0).getRemark() != null) {
                            if (!dateList2.get(0).getRemark().isEmpty()) {
                                layoutRemark.setVisibility(View.VISIBLE);
                                textRemark.setText(dateList2.get(0).getRemark());
                            }
                        } else {
                            layoutRemark.setVisibility(View.GONE);
                        }
                    }
                } else {
                    ToastUtil.display(CalendarActivity.this, res.getMsg());
                }
            }

            @Override
            public void onFailure(Call<ResponseUtil> call, Throwable t) {
                Logger.e("Com", "onFailure: 网络连接错误");
                t.printStackTrace();
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
}
