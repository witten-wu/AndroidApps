package com.example.myapplication;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private String userSelection;
    private boolean fragmentsAdded = false; //
    private boolean isSwipeEnabled = true; // 是否允许滑动
    private String currentSubjectId;

    private List<String> imageNames;

    private Map<String, FeatureData> imageFeatureDataMap = new HashMap<>();

    public static class FeatureData {
        private List<Feature> selectedFeatures;
        private List<String> correctFeaturesText;

        public FeatureData(List<Feature> selectedFeatures, List<String> correctFeaturesText) {
            this.selectedFeatures = selectedFeatures;
            this.correctFeaturesText = correctFeaturesText;
        }

        public List<Feature> getSelectedFeatures() { return selectedFeatures; }
        public List<String> getCorrectFeaturesText() { return correctFeaturesText; }
    }

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

        // 获取 Subject ID
        currentSubjectId = getIntent().getStringExtra("subject_id");
        if (currentSubjectId == null || currentSubjectId.isEmpty()) {
            currentSubjectId = getCurrentSubjectId();
        }

        // 如果没有 Subject ID，返回输入界面
        if (currentSubjectId.isEmpty()) {
            startSubjectIdActivity();
            return;
        }

        userSelection = getIntent().getStringExtra("selection");

        if ("A".equals(userSelection)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 竖屏
            showImageNameInputDialog();
            return; // 暂时不继续执行，等待用户输入
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // 其它情况保持横屏
        }

        // 初始化 ViewPager2
        viewPager = findViewById(R.id.viewPager);

        if ("B".equals(userSelection)) {
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
        } else if ("E".equals(userSelection)) {
            fragmentList.add(new NameScreenPageOne());
        } else if ("F".equals(userSelection)) {
            fragmentList.add(new NameScreenPageTwo());
        } else if ("G".equals(userSelection)) {
            fragmentList.add(new AphasiaBankPageOne());
        } else if ("H".equals(userSelection)) {
            fragmentList.add(new PAPTPageOne());
            fragmentList.add(new PAPTPageTwo());
        } else if ("I".equals(userSelection)) {
            fragmentList.add(new SCPPageOne());
        } else if ("J".equals(userSelection)) {
            fragmentList.add(new SPDPageOne());
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

    private void showImageNameInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("请输入要显示的名词(英文)，多个名词用空格分隔：");

        // 创建输入框
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("例如：tiger box car sun");
        builder.setView(input);

        builder.setPositiveButton("确定", null); // 设置为null，稍后手动处理
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish(); // 取消则退出Activity
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish(); // 返回键也退出Activity
            }
        });

        AlertDialog dialog = builder.create();

        // 手动处理确定按钮点击事件
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button confirmButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String inputText = input.getText().toString().trim();
                        if (inputText.isEmpty()) {
                            // 输入为空，显示提示但不关闭对话框
                            Toast.makeText(MainActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                            input.requestFocus(); // 重新获取焦点
                            return;
                        }

                        // 按空格分割输入的图片名称
                        String[] imageArray = inputText.split("\\s+");
                        List<String> tempImageNames = new ArrayList<>();
                        for (String imageName : imageArray) {
                            if (!imageName.trim().isEmpty()) {
                                tempImageNames.add(imageName.trim());
                            }
                        }

                        // 验证文件夹是否存在
                        List<String> missingFolders = validateImageFolders(tempImageNames);
//                        if (!missingFolders.isEmpty()) {
//                            // 有文件夹不存在，显示错误信息
//                            String missingNames = String.join(", ", missingFolders);
//                            String errorMessage = "以下名词在Noun目录下不存在：\n" + missingNames + "\n请检查输入";
//
//                            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
//                            input.requestFocus(); // 重新获取焦点
//                            return;
//                        }
                        if (!missingFolders.isEmpty()) {
                            // 有文件夹不存在，显示错误信息
                            String missingNames = String.join("\n• ", missingFolders);
                            String errorMessage = "以下名词在Noun目录下不存在：\n\n• " + missingNames + "\n\n请检查输入是否正确";

                            // 使用AlertDialog显示完整错误信息
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("输入验证错误")
                                    .setMessage(errorMessage)
                                    .setPositiveButton("确定", (dialog, which) -> {
                                        input.requestFocus(); // 重新获取焦点
                                    })
                                    .setCancelable(false)
                                    .show();

                            return;
                        }

                        // 所有验证通过，保存图片名称列表
                        imageNames = tempImageNames;

                        // 输入完成后初始化Fragment
                        initializeAfterImageInput();
                        dialog.dismiss(); // 手动关闭对话框
                    }
                });
            }
        });

        dialog.show();
    }

    private List<String> validateImageFolders(List<String> imageNames) {
        List<String> missingFolders = new ArrayList<>();

        try {
            // 获取assets/Noun目录下的所有文件夹
            String[] nounFolders = getAssets().list("Noun");
            if (nounFolders == null) {
                // 如果Noun目录不存在，所有图片都标记为缺失
                missingFolders.addAll(imageNames);
                return missingFolders;
            }

            // 将现有文件夹转换为Set以便快速查找
            Set<String> existingFolders = new HashSet<>();
            for (String folder : nounFolders) {
                existingFolders.add(folder.toLowerCase()); // 转为小写进行比较
            }

            // 检查每个输入的图片名称
            for (String imageName : imageNames) {
                if (!existingFolders.contains(imageName.toLowerCase())) {
                    missingFolders.add(imageName);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            // 如果发生IO异常，标记所有图片为缺失
            missingFolders.addAll(imageNames);
        }

        return missingFolders;
    }

    private void initializeAfterImageInput() {
        // 初始化ViewPager2
        viewPager = findViewById(R.id.viewPager);

        // 创建SFA Fragment序列
        createSFAFragments();

        // 设置适配器
        adapter = new ViewPagerAdapter(this, fragmentList);
        viewPager.setAdapter(adapter);
    }

    private void createSFAFragments() {
        fragmentList.clear(); // 清空现有的Fragment列表

        // 显示加载对话框
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("加载中");
        progressDialog.setMessage("正在加载数据，请稍候...");
        progressDialog.setCancelable(false); // 不允许用户取消
        progressDialog.show();

        // 异步加载数据
        new Thread(() -> {
            try {
                loadAllImageFeatures();

                // 回到主线程创建Fragment并更新UI
                runOnUiThread(() -> {
                    createFragmentsWithData();
                    progressDialog.dismiss(); // 关闭加载对话框

                    // 设置适配器
                    adapter = new ViewPagerAdapter(this, fragmentList);
                    viewPager.setAdapter(adapter);

                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "加载数据失败，请重试", Toast.LENGTH_LONG).show();
                    // 可以考虑退出Activity或者提供重试选项
                    finish();
                });
            }
        }).start();
    }

    private void createFragmentsWithData() {
        for (String imageName : imageNames) {
            FeatureData featureData = imageFeatureDataMap.get(imageName);

            List<Feature> selectedFeatures = new ArrayList<>();
            List<String> correctFeatures = new ArrayList<>();

            if (featureData != null) {
                selectedFeatures = featureData.getSelectedFeatures();
                correctFeatures = featureData.getCorrectFeaturesText();
            }

            // 为每张图片创建7个Fragment
            fragmentList.add(SFAPageOneFragment.newInstance(imageName));
            fragmentList.add(SFAPageTwoFragment.newInstance(imageName, selectedFeatures, correctFeatures));
            fragmentList.add(SFAPageThreeFragment.newInstance(correctFeatures, imageName));
            fragmentList.add(SFAPageFourFragment.newInstance(imageName));
            fragmentList.add(SFAPageFiveFragment.newInstance(imageName, selectedFeatures, correctFeatures));
            fragmentList.add(SFAPageSixFragment.newInstance(imageName));
            fragmentList.add(SFAPageSevenFragment.newInstance(imageName));
        }
    }

    private void loadAllImageFeatures() {
        String csvFileName = "treatment_organized.csv";

        for (String imageName : imageNames) {
            try {
                // 读取当前图片的所有特征
                List<Feature> allFeatures = ExcelReader.getFeaturesForWord(this, csvFileName, imageName);

                // 选择8个正确特征和8个错误特征
                List<Feature> selectedFeatures = ExcelReader.selectFeatures(allFeatures, 8, 8);

                // 提取正确特征的文本
                List<String> correctFeaturesText = new ArrayList<>();
                for (Feature feature : selectedFeatures) {
                    if (feature.hasFeature()) {
                        correctFeaturesText.add(feature.getFeatureZh());
                    }
                }

                // 存储完整的特征数据
                FeatureData featureData = new FeatureData(selectedFeatures, correctFeaturesText);
                imageFeatureDataMap.put(imageName, featureData);

            } catch (Exception e) {
                e.printStackTrace();
                // 如果读取失败，放入空数据
                FeatureData emptyData = new FeatureData(new ArrayList<>(), new ArrayList<>());
                imageFeatureDataMap.put(imageName, emptyData);
            }
        }
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

    private String getCurrentSubjectId() {
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        return preferences.getString("current_subject_id", "");
    }

    public String getActivitySubjectId() {
        return currentSubjectId != null ? currentSubjectId : "unknown";
    }

    private void startSubjectIdActivity() {
        Intent intent = new Intent(MainActivity.this, SubjectIdActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    // 在需要保存数据的方法中使用 Subject ID
//    private void saveDataWithSubjectId(String data) {
//        String fileName = currentSubjectId + "_data_" + System.currentTimeMillis() + ".txt";
//        // 保存数据的逻辑...
//    }

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