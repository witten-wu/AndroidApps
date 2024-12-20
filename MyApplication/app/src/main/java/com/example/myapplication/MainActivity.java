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

    private int[] images = {
            R.drawable.item0, R.drawable.item1,
            R.drawable.item2, R.drawable.item3,
            R.drawable.item4, R.drawable.item5
    };

    private String[] Itemnames = {"茶几", "番茄", "出租车"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userSelection = getIntent().getStringExtra("selection");

        // 根据用户选择动态设置屏幕方向
        if ("A".equals(userSelection)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 设置竖屏
        } else if ("C".equals(userSelection)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 设置竖屏
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 设置横屏
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
                    updateVNESTPageFour2(subject, object);
                    updateVNESTPageFour3(subject, object);
//                    isSwipeEnabled = true;
//                    setViewPagerSwipeEnabled(viewPager, true);
                    viewPager.setCurrentItem(3, true);
                }
            }));
            fragmentList.add(new VNESTPageFour());
            fragmentList.add(new VNESTPageFour2());
            fragmentList.add(new VNESTPageFour3());
            fragmentList.add(new VNESTPageFive());
            fragmentList.add(new VNESTPageSix());
            fragmentList.add(new VNESTPageSeven());
        } else if ("C".equals(userSelection)) {
            fragmentList.add(new RabStorPageOne());
        } else if ("D".equals(userSelection)) {
            fragmentList.add(new LanSdyPageOne(new LanSdyPageOne.SelectionCallback() {
                @Override
                public void onSelect(String option) {
                    updateLanSdyPageOption1(option);
//                    isSwipeEnabled = true;
//                    setViewPagerSwipeEnabled(viewPager, true);
                    viewPager.setCurrentItem(1, true);
                }
            }));
            fragmentList.add(new LanSdyPageOption1());
            fragmentList.add(new LanSdyPageOption1_1());
            fragmentList.add(new LanSdyPageOption1_2());
            fragmentList.add(new LanSdyPageOption1_3());
            fragmentList.add(new LanSdyPageOption1_4());
            fragmentList.add(LanSdyPageOption1_5.newInstance(images, 0, Itemnames));
        }

        // 设置适配器
        adapter = new ViewPagerAdapter(this, fragmentList);
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if ("D".equals(userSelection) && position == 0) {
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

    }
    private void updateVNESTPageFour2(String subject, String object) {
        // 创建一个新的 VNESTPageFour 实例，并传入参数
        VNESTPageFour2 newFragment = VNESTPageFour2.newInstance(subject, object);

        // 替换 Fragment 列表中的 VNESTPageFour
        fragmentList.set(4, newFragment);

        // 通知适配器更新数据
        adapter.notifyItemChanged(4);
    }
    private void updateVNESTPageFour3(String subject, String object) {
        // 创建一个新的 VNESTPageFour 实例，并传入参数
        VNESTPageFour3 newFragment = VNESTPageFour3.newInstance(subject, object);

        // 替换 Fragment 列表中的 VNESTPageFour
        fragmentList.set(5, newFragment);

        // 通知适配器更新数据
        adapter.notifyItemChanged(5);
    }

    private void updateLanSdyPageOption1(String option) {
        // 创建一个新的 VNESTPageFour 实例，并传入参数
        LanSdyPageOption1 newFragment = LanSdyPageOption1.newInstance(option);

        // 替换 Fragment 列表中的 LanSdyPageOne
        fragmentList.set(1, newFragment);

        // 通知适配器更新数据
        adapter.notifyItemChanged(1);

    }

    public void updateLanSdyPageOption1_5(int[] images, int index, String[] itemnames) {
        // 创建一个新的 LanSdyPageOption1_5 实例
        LanSdyPageOption1_5 newFragment = LanSdyPageOption1_5.newInstance(images, index, itemnames);

        // 替换 Fragment 列表中的 LanSdyPageOption1_5
        fragmentList.set(6, newFragment);

        //强制销毁和重建
        adapter = new ViewPagerAdapter(this, fragmentList);
        viewPager.setAdapter(adapter);

        // 切换到 LanSdyPageOption1_5
        viewPager.setCurrentItem(6, false);
    }

    // 禁用或启用 ViewPager2 滑动
    private void setViewPagerSwipeEnabled(ViewPager2 viewPager, boolean enabled) {
        RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);
        if (recyclerView != null) {
            recyclerView.setOnTouchListener((v, event) -> !enabled); //
        }
    }
}