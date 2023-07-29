package com.example.english.ui.essay;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.english.MainActivity;
import com.example.english.R;
import com.example.english.activity.EssayActivity;
import com.example.english.adapter.EssayAdapter;
import com.example.english.model.Essay;
import com.example.english.service.EssayService;
import com.example.english.ui.essay.child.TabFragment;
import com.example.english.util.GsonUtil;
import com.example.english.util.ResponseUtil;
import com.example.english.util.RetrofitRequest;
import com.example.english.util.ToastUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EssayFragment extends Fragment implements EssayAdapter.OnItemClickListener {
    private Call<ResponseUtil> loadDataCall;
    private Context mContext;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    private RecyclerView recyclerView;
    private ArrayList<Essay> essayArrayList;

    private EssayAdapter essayAdapter;

    private String[] tabs = {"英文小说", "情感故事", "英语美文", "双语故事"};
    private List<TabFragment> tabFragmentList = new ArrayList<>();

    private int lastLoadDataItemPosition;

    private static final int UPDATE_DATA = 0x3;

    private Retrofit retrofit;
    private EssayService essayService;

    private String label = "novel";
    private int page = 1;
    private static final int REQUEST_CODE = 1;

    private EssayViewModel essayViewModel;

    private ViewModelProvider.Factory viewModelFactory;

    public void setViewModelFactory(ViewModelProvider.Factory factory) {
        viewModelFactory = factory;
    }

    @Override
    public void onItemClick(Essay essay) {
        String essay_id = essay.getEssay_id();
        // 创建 Intent 对象
        Intent intent = new Intent(requireActivity(), EssayActivity.class);
        // 将选中的 ID 作为参数传递给 Intent
        intent.putExtra("essay_id", essay_id);
        // 启动 Activity
        startActivity(intent);
    }

    // 定义接口
    public interface OnScrollListener {
        void onScrollUp();

        void onScrollDown();
    }

    private OnScrollListener scrollListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_essay, container, false);
        tabLayout = root.findViewById(R.id.tab_layout);
        viewPager2 = root.findViewById(R.id.view_pager);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        essayArrayList = new ArrayList<>();
        essayAdapter = new EssayAdapter(getContext(), essayArrayList);
        essayAdapter.setOnItemClickListener(this);

        //添加tab
        for (int i = 0; i < tabs.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabs[i]));
            tabFragmentList.add(TabFragment.newInstance(tabs[i]));
        }
        // 创建并设置ViewPager2适配器
        viewPager2.setAdapter(new FragmentStateAdapter(getChildFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return tabFragmentList.get(position);
            }

            @Override
            public int getItemCount() {
                return tabFragmentList.size();
            }
        });

        //设置TabLayout和ViewPager联动
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            tab.setText(tabs[position]);
        }).attach();


//        for (int i = 0; i < 20; i++) {
//            String j = "" + i;
//            Essay essay = new Essay(j, j, j, j, j);
//            essayArrayList.add(essay);
//        }
//        essayAdapter.notifyDataSetChanged();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                // 标签选中时触发该回调方法
                int position = tab.getPosition(); // 获取选中的标签索引
                // 执行相应的操作
                String la = tab.getText().toString();
                page = 1;
                switch (la) {
                    case "英文小说":
                        label = "novel";
                        break;
                    case "情感故事":
                        label = "love";
                        break;
                    case "英语美文":
                        label = "essays";
                        break;
                    case "双语故事":
                        label = "shuangyu";
                        break;
                }
                loadData();

                essayAdapter.setOnItemClickListener(EssayFragment.this);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // 标签取消选中时触发该回调方法
                essayArrayList.clear();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // 标签再次选中时触发该回调方法
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == SCROLL_STATE_IDLE &&
                        lastLoadDataItemPosition == essayAdapter.getItemCount()) {
                    page++;
                    loadData();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
                    int firstVisibleItem = manager.findFirstVisibleItemPosition();
                    int l = manager.findLastCompletelyVisibleItemPosition();
                    lastLoadDataItemPosition = firstVisibleItem + (l - firstVisibleItem) + 1;
                }
                handleScroll(dy);
            }
        });
        recyclerView.setAdapter(essayAdapter);
        return root;
    }

    // 在onAttach()方法中将Activity设置为监听器
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        retrofit = RetrofitRequest.getInstance(mContext);
        essayService = retrofit.create(EssayService.class);
        if (context instanceof OnScrollListener) {
            scrollListener = (OnScrollListener) context;
        }
    }

    // 在滚动监听器中触发接口方法
    private void handleScroll(int dy) {
        if (dy > 0) {
            // 向上滚动，通知Activity隐藏底部导航栏
            if (scrollListener != null) {
                hideTab();
                scrollListener.onScrollUp();
            }
        } else if (dy < 0) {
            // 向下滚动，通知Activity显示底部导航栏
            if (scrollListener != null) {
                showTab();
                scrollListener.onScrollDown();
            }
        }
    }

    public void hideTab() {
        // 隐藏Tab的逻辑
        tabLayout.setVisibility(View.GONE);
    }

    public void showTab() {
        // 显示Tab的逻辑
        tabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 在Fragment的生命周期方法中加载数据
        loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 执行数据刷新操作
        loadData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // 执行数据刷新操作
            loadData();
        }
    }

    private void loadData() {
        // 执行异步操作，加载数据
        // 示例：模拟从网络或数据库获取数据
        // 可以使用线程、协程、异步任务等方式进行异步加载
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 模拟耗时操作
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Call<ResponseUtil> call = essayService.getEssays(label, String.valueOf(page), "20");
                call.enqueue(new Callback<ResponseUtil>() {
                    @Override
                    public void onResponse(Call<ResponseUtil> call, Response<ResponseUtil> response) {
                        ResponseUtil res = response.body();
                        if (res.getCode() == 200) {
                            String essay = GsonUtil.object2Json(res.getData());
                            List<Essay> essayList = new Gson().fromJson(essay, new TypeToken<List<Essay>>() {
                            }.getType());
                            if (essayList != null) {
                                for (int i = 0; i < essayList.size(); i++) {
                                    Essay es = essayList.get(i);
                                    System.out.println(es + "\n\n\n\n\n\n");
                                    essayArrayList.add(es);
                                }
                            }
                        } else {
                            ToastUtil.display(getActivity(), res.getMsg());
                        }

                            // 在主线程更新UI
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // 添加新数据到列表
                                    // 刷新适配器
                                    essayAdapter.notifyDataSetChanged();
                                }
                            });

                    }

                    @Override
                    public void onFailure(Call<ResponseUtil> call, Throwable t) {
                        Logger.e("Com", "onFailure: 网络连接错误");
                        t.printStackTrace();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        essayArrayList.clear();
    }

}