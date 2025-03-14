package com.example.myapplication;

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
    private String userSelection;
    private boolean fragmentsAdded = false; //
    private boolean isSwipeEnabled = true; // 是否允许滑动

    private int[] G1Aimages = {
            R.drawable.g1aitem0, R.drawable.g1aitem1,
            R.drawable.g1aitem2, R.drawable.g1aitem3,
            R.drawable.g1aitem4, R.drawable.g1aitem5,
            R.drawable.g1aitem6, R.drawable.g1aitem7,
            R.drawable.g1aitem8, R.drawable.g1aitem9
    };
    private String[] G1AItemnames = {"菠蘿", "番茄", "巴士", "腸粉", "芝麻糊"};
    private int[] G1ACorrect = {1, 0, 1, 0, 0};
    private int[] G1AAudio = {R.raw.g1a_01, R.raw.g1a_02, R.raw.g1a_03, R.raw.g1a_04, R.raw.g1a_05};

    private int[] G2Aimages = {
            R.drawable.g2aitem0, R.drawable.g2aitem1, R.drawable.g2aitem2, R.drawable.g2aitem5,
            R.drawable.g2aitem4, R.drawable.g2aitem5, R.drawable.g2aitem6, R.drawable.g2aitem13,
            R.drawable.g2aitem8, R.drawable.g2aitem9, R.drawable.g2aitem10, R.drawable.g2aitem2,
            R.drawable.g2aitem12, R.drawable.g2aitem13, R.drawable.g2aitem14, R.drawable.g2aitem5,
            R.drawable.g2aitem16, R.drawable.g2aitem17, R.drawable.g2aitem18, R.drawable.g2aitem8
    };
    private String[] G2AItemnames = {"狗被馬追", "弟弟被老鼠追", "羊被雞仔追", "兔仔被狗追", "弟弟被姐姐打"};
    private int[] G2ACorrect = {0, 2, 1, 0, 0};
    private int[] G2AAudio = {R.raw.g2a_01, R.raw.g2a_02, R.raw.g2a_03, R.raw.g2a_04, R.raw.g2a_05};

    private int[] G2Bimages = {
            R.drawable.g2bitem1, R.drawable.g2bitem0, R.drawable.g2bitem2, R.drawable.g2bitem3,
            R.drawable.g2bitem6, R.drawable.g2bitem5, R.drawable.g2bitem4, R.drawable.g2bitem7,
            R.drawable.g2bitem8, R.drawable.g2bitem9, R.drawable.g2bitem10, R.drawable.g2bitem11,
            R.drawable.g2bitem15, R.drawable.g2bitem13, R.drawable.g2bitem14, R.drawable.g2bitem12,
            R.drawable.g2bitem18, R.drawable.g2bitem17, R.drawable.g2bitem16, R.drawable.g2bitem19
    };
    private String[] G2BItemnames = {"男仔高過女仔少少", "男仔高過女仔好多", "女仔高過男仔少少", "女仔高過男仔好多", "女仔矮過男仔好多"};
    private int[] G2BCorrect = {1, 2, 0, 3, 2};
    private int[] G2BAudio = {R.raw.g2b_01, R.raw.g2b_02, R.raw.g2b_03, R.raw.g2b_04, R.raw.g2b_05};

    private int[] G2Cimages = {
            R.drawable.g2citem0, R.drawable.g2citem1,
            R.drawable.g2citem2, R.drawable.g2citem3,
            R.drawable.g2citem4, R.drawable.g2citem5,
            R.drawable.g2citem6, R.drawable.g2citem7,
            R.drawable.g2citem8, R.drawable.g2citem9
    };
    private String[] G2CItemnames = {"你想買菜心。有人話:「超級市場一定有菜心賣。」 又有人話:「街巿應該有菜心賣。」你會去邊度買菜心呢?",
            "我哋而家要搵公公。有人話:「公公應該去咗公園。」 又有人話:「公公實去咗茶樓。」咁你會去邊度搵公公呢?",
            "妹妹出咗去。有人話:「妹妹可能去咗游水。」又有人話:「妹妹一定去咗彈琴。」你估妹妹去咗做咩?",
            "你想食零食。有人話：「你一定要食糖。」又有人話：「你應該食朱古力。」咁你要食咩呢？",
            "我哋唔見左本中文書。有人話:「本中文書可能喺學校。」 又有人話:「本中文書一定唔喺屋企。」咁你會去邊度搵本中文書呀?"};
    private int[] G2CCorrect = {0,1,1,0,0};
    private int[] G2CAudio = {R.raw.g2c_01, R.raw.g2c_02, R.raw.g2c_03, R.raw.g2c_04, R.raw.g2c_05};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userSelection = getIntent().getStringExtra("selection");

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
                    addFragmentsBasedOnOption(option);
                    updateLanSdyPageOption1(option);
                    viewPager.setCurrentItem(1, true);
                }
            }));
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

    // 根据选项添加不同的Fragment
    private void addFragmentsBasedOnOption(String option) {
        // 如果已经添加过Fragment，则不重复添加
        if (fragmentsAdded) {
            return;
        }

        // 添加基本Fragment
        fragmentList.add(new LanSdyPageOption1());  // 位置1

        // 根据不同的option添加不同的Fragment
        if ("聽字選圖".equals(option)) {
            // Option 1的Fragment列表
            fragmentList.add(new LanSdyPageOption1_1());
            fragmentList.add(new LanSdyPageOption1_2());
            fragmentList.add(new LanSdyPageOption1_3());
            fragmentList.add(new LanSdyPageOption1_3_1());
            fragmentList.add(new LanSdyPageOption1_3_2());
            fragmentList.add(new LanSdyPageOption1_3_3());
            fragmentList.add(new LanSdyPageOption1_4());
            fragmentList.add(LanSdyPageOption1_5.newInstance(G1Aimages, 0, G1AItemnames, G1ACorrect, G1AAudio));
        } else if ("被動句".equals(option)) {
            fragmentList.add(new LanSdyPageOption2_1());
            fragmentList.add(new LanSdyPageOption2_2());
            fragmentList.add(new LanSdyPageOption2_3());
            fragmentList.add(new LanSdyPageOption2_3_1());
            fragmentList.add(new LanSdyPageOption2_3_2());
            fragmentList.add(new LanSdyPageOption2_3_3());
            fragmentList.add(new LanSdyPageOption2_4());
            fragmentList.add(LanSdyPageOption2_5.newInstance(G2Aimages, 0, G2AItemnames, G2ACorrect, G2AAudio));
        } else if ("比較句".equals(option)) {
            fragmentList.add(new LanSdyPageOption3_1());
            fragmentList.add(new LanSdyPageOption3_2());
            fragmentList.add(new LanSdyPageOption3_3());
            fragmentList.add(new LanSdyPageOption3_3_1());
            fragmentList.add(new LanSdyPageOption3_3_2());
            fragmentList.add(new LanSdyPageOption3_3_3());
            fragmentList.add(new LanSdyPageOption3_4());
            fragmentList.add(LanSdyPageOption3_5.newInstance(G2Bimages, 0, G2BItemnames, G2BCorrect, G2BAudio));
        } else if ("情態詞".equals(option)) {
            fragmentList.add(new LanSdyPageOption4_1());
            fragmentList.add(new LanSdyPageOption4_2());
            fragmentList.add(new LanSdyPageOption4_3());
            fragmentList.add(new LanSdyPageOption4_3_1());
            fragmentList.add(new LanSdyPageOption4_3_2());
            fragmentList.add(new LanSdyPageOption4_3_3());
            fragmentList.add(new LanSdyPageOption4_4());
            fragmentList.add(LanSdyPageOption4_5.newInstance(G2Cimages, 0, G2CItemnames, G2CCorrect, G2CAudio));
        }

        // 更新适配器
        adapter = new ViewPagerAdapter(this, fragmentList);
        viewPager.setAdapter(adapter);

        // 标记为已添加
        fragmentsAdded = true;
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

    public void updateLanSdyPageOption1_5(int[] images, int index, String[] itemnames, int[] correctAnswer, int[] titleAudioResId) {
        // 创建一个新的 LanSdyPageOption1_5 实例
        LanSdyPageOption1_5 newFragment = LanSdyPageOption1_5.newInstance(images, index, itemnames, correctAnswer, titleAudioResId);

        // 替换 Fragment 列表中的 LanSdyPageOption1_5
        fragmentList.set(9, newFragment);

        //强制销毁和重建
        adapter = new ViewPagerAdapter(this, fragmentList);
        viewPager.setAdapter(adapter);

        // 切换到 LanSdyPageOption1_5
        viewPager.setCurrentItem(9, false);
    }

    public void updateLanSdyPageOption2_5(int[] images, int index, String[] itemnames, int[] correctAnswer, int[] titleAudioResId) {
        LanSdyPageOption2_5 newFragment = LanSdyPageOption2_5.newInstance(images, index, itemnames, correctAnswer, titleAudioResId);
        fragmentList.set(9, newFragment);
        adapter = new ViewPagerAdapter(this, fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(9, false);
    }

    public void updateLanSdyPageOption3_5(int[] images, int index, String[] itemnames, int[] correctAnswer, int[] titleAudioResId) {
        LanSdyPageOption3_5 newFragment = LanSdyPageOption3_5.newInstance(images, index, itemnames, correctAnswer, titleAudioResId);
        fragmentList.set(9, newFragment);
        adapter = new ViewPagerAdapter(this, fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(9, false);
    }

    public void updateLanSdyPageOption4_5(int[] images, int index, String[] itemnames, int[] correctAnswer, int[] titleAudioResId) {
        LanSdyPageOption4_5 newFragment = LanSdyPageOption4_5.newInstance(images, index, itemnames, correctAnswer, titleAudioResId);
        fragmentList.set(9, newFragment);
        adapter = new ViewPagerAdapter(this, fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(9, false);
    }

    // 禁用或启用 ViewPager2 滑动
    private void setViewPagerSwipeEnabled(ViewPager2 viewPager, boolean enabled) {
        RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);
        if (recyclerView != null) {
            recyclerView.setOnTouchListener((v, event) -> !enabled); //
        }
    }
}