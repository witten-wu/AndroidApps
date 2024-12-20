package com.example.myapplication;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class RabStorPageOne extends Fragment {

    // 图片资源数组
    private final int[] images = {
            R.drawable.rbs_1, R.drawable.rbs_2, R.drawable.rbs_3,
            R.drawable.rbs_4, R.drawable.rbs_5, R.drawable.rbs_6,
            R.drawable.rbs_7, R.drawable.rbs_8
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.rs_page1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 找到 ViewPager2
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);

        // 初始化适配器并设置到 ViewPager2
        ImagePagerAdapter adapter = new ImagePagerAdapter(getContext(), images);
        viewPager.setAdapter(adapter);

        viewPager.getChildAt(0).setOnTouchListener((v, event) -> {
            // 告诉父视图不要拦截触摸事件
            v.getParent().requestDisallowInterceptTouchEvent(true);

            // 检测点击事件
            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.performClick(); // 调用 performClick() 处理点击
            }

            return false; // 返回 false 以允许其他事件（如滑动）继续处理
        });

    }
}