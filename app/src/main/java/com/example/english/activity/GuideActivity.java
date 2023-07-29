package com.example.english.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.english.R;
import com.example.english.adapter.GuideAdapter;
import com.example.english.layout.GuideFragmentOne;
import com.example.english.layout.GuideFragmentTwo;
import com.example.english.layout.GuideFragmentThree;
import com.example.english.util.ActivityCollector;
import com.example.english.util.CodeCountDownUtil;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {
    private ViewPager2 myPager1;

    List<Fragment> fragments = new ArrayList<>();

    private ImageView[] dots;
    private int currentIndex;
    private LinearLayout mDotLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);
        myPager1 = findViewById(R.id.my_pager1);
        mDotLayout = (LinearLayout) findViewById(R.id.dots_layout);

        //加入Fragment
        fragments.add(new GuideFragmentOne());
        fragments.add(new GuideFragmentTwo());
        fragments.add(new GuideFragmentThree());
        initDots();

        //实例化适配器
        GuideAdapter guideAdapter = new GuideAdapter(getSupportFragmentManager(),getLifecycle(),fragments);

        //设置适配器
        myPager1.setAdapter(guideAdapter);

        myPager1.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                setCurrentDot(position);
            }
        });
        ActivityCollector.addActivity(this);
    }

    private void initDots(){
        dots = new ImageView[fragments.size()];
        for (int i = 0; i < fragments.size(); i++) {
            dots[i] = (ImageView) mDotLayout.getChildAt(i);
            if (i != 0) {
                dots[i].setImageResource(R.drawable.ic_circle_dot);
            }
        }
        currentIndex = 0;
        dots[currentIndex].setImageResource(R.drawable.ic_rect_dot);
    }

    private void setCurrentDot(int position) {
        if (position < 0 || position > fragments.size() - 1
                || currentIndex == position) {
            return;
        }
        dots[position].setImageDrawable(null);
        dots[position].setImageResource(R.drawable.ic_rect_dot);
        dots[currentIndex].setImageDrawable(null);
        dots[currentIndex].setImageResource(R.drawable.ic_circle_dot);
        currentIndex = position;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}