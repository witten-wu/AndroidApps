package com.example.myapplication;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获取用户的选择 ("A" 或 "B")
        String userSelection = getIntent().getStringExtra("selection");

        // 初始化 ViewPager2
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        // 动态构建 Fragment 列表
        List<Fragment> fragmentList = new ArrayList<>();
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
            fragmentList.add(new VNESTPageThree());
            fragmentList.add(new VNESTPageFour());
            fragmentList.add(new VNESTPageFive());
            fragmentList.add(new VNESTPageSix());
            fragmentList.add(new VNESTPageSeven());
        }

        // 设置适配器
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, fragmentList);
        viewPager.setAdapter(adapter);
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
}