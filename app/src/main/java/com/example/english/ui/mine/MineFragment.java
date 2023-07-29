package com.example.english.ui.mine;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.english.MainActivity;
import com.example.english.R;
import com.example.english.activity.AboutActivity;
import com.example.english.activity.CalendarActivity;
import com.example.english.activity.PlanActivity;
import com.example.english.model.Essay;
import com.example.english.model.LoginUser;
import com.example.english.model.MineInfo;
import com.example.english.service.EssayService;
import com.example.english.service.UserService;
import com.example.english.util.GsonUtil;
import com.example.english.util.MyApplication;
import com.example.english.util.ResponseUtil;
import com.example.english.util.RetrofitRequest;
import com.example.english.util.TimeUtil;
import com.example.english.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MineFragment extends Fragment {

    private TextView name, days, con_day, integral;
    private LinearLayout layoutPlan, layoutAnalyse, layoutCalendar, layoutCollect, layoutMoney;
    private RelativeLayout layoutInformation, layoutNotify, layoutAbout;
    private RelativeLayout layoutSyno, layoutFeedback;
    private Switch aSwitchOpen;

    private Retrofit retrofit;
    private UserService userService;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mine, container, false);
        name = root.findViewById(R.id.text_me_name);
        days = root.findViewById(R.id.text_me_days);
        con_day = root.findViewById(R.id.text_me_continue);
        integral = root.findViewById(R.id.text_me_integral);
        layoutPlan = root.findViewById(R.id.layout_me_plan);
        layoutAnalyse = root.findViewById(R.id.layout_me_analyse);
        layoutCalendar = root.findViewById(R.id.layout_me_calendar);
        layoutCollect = root.findViewById(R.id.layout_me_collect_list);
        layoutInformation = root.findViewById(R.id.layout_me_info);
        layoutNotify = root.findViewById(R.id.layout_me_notify);
        layoutSyno = root.findViewById(R.id.layout_me_syno);
        layoutAbout = root.findViewById(R.id.layout_me_about);
        layoutFeedback = root.findViewById(R.id.layout_me_feedback);
        aSwitchOpen = root.findViewById(R.id.switch_open);
        layoutMoney = root.findViewById(R.id.layout_me_money);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

        layoutMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] date = TimeUtil.getStringDate(TimeUtil.getCurrentDateStamp()).split("-");
                final int currentMoney = Integer.parseInt(integral.getText().toString().trim());
                if (currentMoney >= 10) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("提示")
                            .setMessage("确定要花费10积分进行日历补卡吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                                            new DatePickerDialog.OnDateSetListener() {
                                                @Override
                                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                                    new MyAsyncTask().execute(year, month, dayOfMonth);
                                                }
                                            },
                                            Integer.parseInt(date[0]),
                                            Integer.parseInt(date[1]) - 1,
                                            Integer.parseInt(date[2]));
                                    DatePicker datePicker = datePickerDialog.getDatePicker();
                                    datePicker.setMaxDate(new Date().getTime());
                                    datePickerDialog.show();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                } else {
                    ToastUtil.another_display(MyApplication.getContext(), "抱歉，你的积分还不足要求。必须满足10点积分才可以补打卡哦");
                }
            }
        });
    }

    private class MyAsyncTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            int year = params[0];
            int month = params[1];
            int dayOfMonth = params[2];
            System.out.println(year);
            System.out.println(month);
            System.out.println(dayOfMonth);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // 更新 UI 或执行其他操作
            // ...
        }
    }

    // 在onAttach()方法中将Activity设置为监听器
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        retrofit = RetrofitRequest.getInstance(mContext);
        userService = retrofit.create(UserService.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateData();
    }

    private void updateData() {
        // 设置天数
        Call<ResponseUtil> call = userService.getMine();
        call.enqueue(new Callback<ResponseUtil>() {
            @Override
            public void onResponse(Call<ResponseUtil> call, Response<ResponseUtil> response) {
                ResponseUtil res = response.body();
                Object data = res.getData();
                String mineJson = new String();
                if (data != null) {
                    mineJson = GsonUtil.object2Json(data);
                }
                MineInfo mineInfo = new Gson().fromJson(mineJson, MineInfo.class);
                if (res.getCode() == 200) {
                    name.setText(mineInfo.getNickname());
                    days.setText(mineInfo.getDays());
                    con_day.setText(mineInfo.getWords());
                    integral.setText(mineInfo.getIntegral());
                } else {
                    ToastUtil.display(getActivity(), res.getMsg());
                }
            }
            @Override
            public void onFailure(Call<ResponseUtil> call, Throwable t) {
                Logger.e("Mine", "onFailure: 网络连接错误");
                t.printStackTrace();
            }
        });
    }

//    @Override
//    public void onClick(View v) {
//        Intent intent = new Intent();
//        switch (v.getId()) {
//            case R.id.layout_me_calendar:
//                intent.setClass(getActivity(), CalendarActivity.class);
//                break;
//            case R.id.layout_me_collect_list:
//                intent.setClass(getActivity(), ListActivity.class);
//                break;
//            case R.id.layout_me_analyse:
//                intent.setClass(getActivity(), ChartActivity.class);
//                break;
//            case R.id.layout_me_plan:
//                intent.setClass(getActivity(), PlanActivity.class);
//                break;
//            case R.id.layout_me_about:
//                intent.setClass(getActivity(), AboutActivity.class);
//                break;
//            case R.id.layout_me_info:
//                intent.setClass(getActivity(), InfoActivity.class);
//                break;
//        }
//        startActivity(intent);
//    }

    private void init() {
        layoutCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), CalendarActivity.class);
                startActivity(intent);
            }
        });

        layoutAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });

        layoutPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), PlanActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}