package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PAPTPageTwo extends Fragment {
    private static final String TAG = "PAPTPageTwo";
    private List<String> imagePaths = new ArrayList<>();
    private String subjectId;
    private List<TrialData> trialDataList = new ArrayList<>();
    private TrialData currentTrial;
    private int currentImageIndex = 0;
    private boolean isExperimentCompleted = false;
    private long totalTimeOnPage = 0; // 累计停留时间
    private long lastResumeTime; // 最后一次resume的时间
    private ViewPager2 viewPager;
    private Button buttonA, buttonB;
    public static class TrialData {
        private String imageName;
        private String userChoice; // "A" 或 "B"
        private long reactionTime; // 反应时间(毫秒)
        private long startTime; // 开始时间
        private long endTime; // 结束时间
        private int trialIndex; // 试验序号

        public TrialData(String imageName, int trialIndex) {
            this.imageName = imageName;
            this.trialIndex = trialIndex;
            this.startTime = System.currentTimeMillis();
        }

        public void setUserChoice(String userChoice) {
            this.userChoice = userChoice;
            this.endTime = System.currentTimeMillis();
            this.reactionTime = this.endTime - this.startTime;
        }

        // 转换为JSON对象
        public JSONObject toJSON() throws JSONException {
            JSONObject json = new JSONObject();
            json.put("imageName", imageName);
            json.put("userChoice", userChoice);
            json.put("reactionTime", reactionTime);
            json.put("trialIndex", trialIndex);
            return json;
        }

        // Getter方法
        public String getImageName() { return imageName; }
        public String getUserChoice() { return userChoice; }
        public long getReactionTime() { return reactionTime; }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.papt_page0, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        subjectId = getSubjectIdFromActivity();

        loadImagesFromAssets();

        setupUI(view);
    }

    private void setupUI(View view) {
        // 找到 ViewPager2 和按钮
        viewPager = view.findViewById(R.id.viewPager);
        buttonA = view.findViewById(R.id.buttonA);
        buttonB = view.findViewById(R.id.buttonB);

        // 禁止手动滑动
        viewPager.setUserInputEnabled(false);

        // 使用新的适配器
        AssetImagePagerAdapter adapter = new AssetImagePagerAdapter(getContext(), imagePaths, R.layout.papt_image);
        viewPager.setAdapter(adapter);

        // 设置页面变化监听器
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentImageIndex = position;
                startNewTrial();
            }
        });

        // 设置按钮点击监听器
        buttonA.setOnClickListener(v -> {
            recordUserChoice("A");
            proceedToNext();
        });

        buttonB.setOnClickListener(v -> {
            recordUserChoice("B");
            proceedToNext();
        });

        // 开始第一个试验
        startNewTrial();

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

    private void startNewTrial() {
        if (currentImageIndex < imagePaths.size()) {
            String imageName = imagePaths.get(currentImageIndex);
            currentTrial = new TrialData(imageName, currentImageIndex);

            Log.d(TAG, "开始新试验: " + imageName + " (第" + (currentImageIndex + 1) + "张)");
        }
    }

    private void recordUserChoice(String choice) {
        if (currentTrial != null) {
            currentTrial.setUserChoice(choice);
            trialDataList.add(currentTrial);

            Log.d(TAG, String.format("记录数据 - 图片: %s, 选择: %s, 反应时间: %dms",
                    currentTrial.getImageName(),
                    currentTrial.getUserChoice(),
                    currentTrial.getReactionTime()));
        }
    }

    private void proceedToNext() {
        if (currentImageIndex < imagePaths.size() - 1) {
            // 跳转到下一张图片
            viewPager.setCurrentItem(currentImageIndex + 1);
        } else {
            // 所有图片完成，保存数据
            isExperimentCompleted = true;
            saveAllTrialData();
            Toast.makeText(getContext(), "实验完成！数据已保存", Toast.LENGTH_SHORT).show();

            // 延迟后返回到SelectionActivity
            new android.os.Handler().postDelayed(() -> {
                if (getActivity() != null) {
                    getActivity().finish(); // 结束当前Activity，返回到上一个Activity
                }
            }, 1500);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        lastResumeTime = System.currentTimeMillis();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 累计停留时间
        if (lastResumeTime > 0) {
            totalTimeOnPage += System.currentTimeMillis() - lastResumeTime;
            lastResumeTime = 0;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 在Fragment销毁时保存当前数据
        if (!isExperimentCompleted && !trialDataList.isEmpty()) {
            savePartialData();
        }
    }

    private void savePartialData() {
        try {
            long finalTotalTime = totalTimeOnPage;
            if (lastResumeTime > 0) {
                finalTotalTime += System.currentTimeMillis() - lastResumeTime;
            }

            JSONObject partialData = new JSONObject();
            partialData.put("subjectId", subjectId);
            partialData.put("experimentType", "PAPT");
            partialData.put("totalTrials", trialDataList.size());
            partialData.put("isCompleted", false);
            partialData.put("totalDuration", finalTotalTime);

            // 添加已完成的试验数据
            JSONArray trialsArray = new JSONArray();
            for (TrialData trial : trialDataList) {
                trialsArray.put(trial.toJSON());
            }
            partialData.put("trials", trialsArray);

            saveToFile(partialData.toString());
            Log.d(TAG, "Partial data saved due to early exit");

        } catch (JSONException e) {
            Log.e(TAG, "Error creating partial JSON data", e);
        }
    }

    private void saveAllTrialData() {
        try {
            long finalTotalTime = totalTimeOnPage;
            if (lastResumeTime > 0) {
                finalTotalTime += System.currentTimeMillis() - lastResumeTime;
            }

            JSONObject experimentData = new JSONObject();
            experimentData.put("subjectId", subjectId);
            experimentData.put("experimentType", "PAPT");
            experimentData.put("totalTrials", trialDataList.size());
            experimentData.put("isCompleted", isExperimentCompleted);
            experimentData.put("totalDuration", finalTotalTime);

            // 添加试验数据数组
            JSONArray trialsArray = new JSONArray();
            for (TrialData trial : trialDataList) {
                trialsArray.put(trial.toJSON());
            }
            experimentData.put("trials", trialsArray);

            // 保存到文件
            saveToFile(experimentData.toString());

        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON data", e);
        }
    }

    private String getSubjectIdFromActivity() {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            return mainActivity.getActivitySubjectId();
        }
        return "unknown";
    }

    private void saveToFile(String jsonData) {
        try {
            String deviceId = getDeviceIdentifier();
            long timestamp = System.currentTimeMillis();

            File deviceFolder = new File(requireContext().getExternalFilesDir(null), deviceId);
            File subjectFolder = new File(deviceFolder, subjectId);
            File experimentFolder = new File(subjectFolder, "PAPT");

            if (!experimentFolder.exists()) {
                boolean created = experimentFolder.mkdirs();
                if (!created) {
                    Log.e(TAG, "Failed to create folder structure");
                    return;
                }
            }

            String fileName = timestamp + ".json";
            File file = new File(experimentFolder, fileName);

            FileWriter writer = new FileWriter(file);
            writer.write(jsonData);
            writer.close();

            Log.d(TAG, "Data saved to: " + file.getAbsolutePath());

        } catch (IOException e) {
            Log.e(TAG, "Error writing file", e);
        }
    }

    private String getDeviceIdentifier() {
        try {
            String androidId = Settings.Secure.getString(
                    requireContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID
            );

            // 确保 Android ID 有效（不为空且不是已知的无效值）
            if (androidId != null && !androidId.isEmpty() && !"9774d56d682e549c".equals(androidId)) {
                return androidId;
            } else {
                // 如果获取不到有效的 Android ID，使用一个默认值
                Log.w(TAG, "Invalid or null Android ID, using default");
                return "unknown";
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to get Android ID", e);
            return "unknown";
        }
    }

    private void loadImagesFromAssets() {
        AssetManager assetManager = requireContext().getAssets();

        try {
            String[] files = assetManager.list("PAPT");
            if (files != null) {
                for (String file : files) {
                    // 检查是否为图片文件
                    if (isImageFile(file)) {
                        imagePaths.add("PAPT/" + file);
                    }
                }
            }

            // 可选：打乱图片顺序
            Collections.shuffle(imagePaths);

            Log.d(TAG, "Loaded " + imagePaths.size() + " images from assets");

        } catch (IOException e) {
            Log.e(TAG, "Error loading images from assets", e);
            Toast.makeText(requireContext(), "加载图片失败", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isImageFile(String fileName) {
        String[] imageExtensions = {".jpg", ".jpeg", ".png", ".bmp", ".webp", ".gif"};
        String lowerFileName = fileName.toLowerCase();
        for (String ext : imageExtensions) {
            if (lowerFileName.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
}