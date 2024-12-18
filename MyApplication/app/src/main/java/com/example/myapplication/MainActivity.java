package com.example.myapplication;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private String userSelection; // 用户选择的 A 或 B
    private boolean isSwipeEnabled = true; // 是否允许滑动

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获取用户的选择 ("A" 或 "B")
        userSelection = getIntent().getStringExtra("selection");

        // 根据用户选择动态设置屏幕方向
        if ("B".equals(userSelection)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 设置横屏
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 设置竖屏
        }

        // 初始化 ViewPager2
        viewPager = findViewById(R.id.viewPager);

        if ("A".equals(userSelection)) {
            fragmentList.add(new SFAPageOneFragment());
            fragmentList.add(new SFAPageTwoFragment());
            fragmentList.add(new SFAPageThreeFragment());
            fragmentList.add(new SFAPageFourFragment());
            fragmentList.add(new SFAPageFiveFragment());
            fragmentList.add(new SFAPageSixFragment());
            fragmentList.add(new SFAPageSevenFragment());
        } else if ("B".equals(userSelection)) {
            fragmentList.add(new VNESTPageOne());
            fragmentList.add(new VNESTPageTwo());
            fragmentList.add(new VNESTPageThree(new VNESTPageThree.SelectionCallback() {
                @Override
                public void onSelect(String subject, String object) {
                    updateVNESTPageFour(subject, object);
                    isSwipeEnabled = true;
                    setViewPagerSwipeEnabled(viewPager, true);
                }
            }));
            fragmentList.add(new VNESTPageFour());
            fragmentList.add(new VNESTPageFive());
            fragmentList.add(new VNESTPageSix());
            fragmentList.add(new VNESTPageSeven());
        }

        // 设置适配器
        adapter = new ViewPagerAdapter(this, fragmentList);
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                // 检测是否滑动到 VNESTPageThree
                if ("B".equals(userSelection) && position == 2) { // 第三个 Fragment 是 VNESTPageThree
                    isSwipeEnabled = false; // 禁用滑动
                    setViewPagerSwipeEnabled(viewPager, false);
                }
            }
        });
    }

    // ViewPager2 的适配器
    private static class ViewPagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragments;

        public ViewPagerAdapter(@NonNull AppCompatActivity activity, List<Fragment> fragments) {
            super(activity);
            this.fragments = fragments;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }

    private void updateVNESTPageFour(String subject, String object) {
        // 创建一个新的 VNESTPageFour 实例，并传入参数
        VNESTPageFour newFragment = VNESTPageFour.newInstance(subject, object);

        // 替换 Fragment 列表中的 VNESTPageFour
        fragmentList.set(3, newFragment);

        // 通知适配器更新数据
        adapter.notifyItemChanged(3);

        // 自动跳转到 VNESTPageFour
        viewPager.setCurrentItem(3, true);
    }

    // 禁用或启用 ViewPager2 滑动
    private void setViewPagerSwipeEnabled(ViewPager2 viewPager, boolean enabled) {
        RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);
        if (recyclerView != null) {
            recyclerView.setOnTouchListener((v, event) -> !enabled); // 返回 true 禁用滑动
        }
    }
}