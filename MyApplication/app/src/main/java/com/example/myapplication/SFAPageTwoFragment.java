package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.android.flexbox.FlexboxLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SFAPageTwoFragment extends Fragment {
    private static final String ARG_IMAGE_NAME = "image_name";
    private static final String ARG_CORRECT_FEATURES = "correct_features";
    private static final String ARG_SELECTED_FEATURES = "selected_features";
    private String imageName;
    private FlexboxLayout featuresContainer;       // 特征条目容器
    //private android.widget.GridLayout trashcanItemsContainer; // 垃圾桶条目容器
    private FlexboxLayout trashcanItemsContainer; // 垃圾桶条目容器
    private View trashcanImage;             // 垃圾桶图片
    private ImageView mainImage;
    private List<String> trashcanContents;       // 用于记录垃圾桶的内容
    private List<Feature> selectedFeatures;
    private List<String> correctFeaturesText;
    private String subjectId;
    private static final String TAG = "SFAPageTwoFragment";
    private long totalTimeOnPage = 0; // 累计停留时间
    private long lastResumeTime; // 最后一次resume的时间
    private int submitClickCount = 0;
    private boolean hasUserInteracted = false; // 用户是否有过交互

    public static SFAPageTwoFragment newInstance(String imageName, List<Feature> selectedFeatures, List<String> correctFeatures) {
        SFAPageTwoFragment fragment = new SFAPageTwoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_NAME, imageName);
        args.putStringArrayList(ARG_CORRECT_FEATURES, new ArrayList<>(correctFeatures));

        // 将 selectedFeatures 转换为可序列化的格式
        ArrayList<Bundle> selectedFeaturesBundles = new ArrayList<>();
        for (Feature feature : selectedFeatures) {
            Bundle featureBundle = new Bundle();
            featureBundle.putString("featureZh", feature.getFeatureZh());
            featureBundle.putBoolean("hasFeature", feature.hasFeature());
            // 添加其他需要的字段
            selectedFeaturesBundles.add(featureBundle);
        }
        args.putParcelableArrayList(ARG_SELECTED_FEATURES, selectedFeaturesBundles);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageName = getArguments().getString(ARG_IMAGE_NAME);

            // 获取正确特征文本
            ArrayList<String> features = getArguments().getStringArrayList(ARG_CORRECT_FEATURES);
            if (features != null) {
                correctFeaturesText = features;
            }

            // 获取选中的特征列表
            ArrayList<Bundle> selectedFeaturesBundles = getArguments().getParcelableArrayList(ARG_SELECTED_FEATURES);
            if (selectedFeaturesBundles != null) {
                selectedFeatures = new ArrayList<>();
                for (Bundle featureBundle : selectedFeaturesBundles) {
                    Feature feature = new Feature();
                    feature.setFeatureZh(featureBundle.getString("featureZh"));
                    feature.setHasFeature(featureBundle.getBoolean("hasFeature"));
                    // 设置其他字段
                    selectedFeatures.add(feature);
                }
            }
        }

        hasUserInteracted = false;
        submitClickCount = 0;
        totalTimeOnPage = 0;
    }

    public SFAPageTwoFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull android.view.LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 加载 page_two.xml 布局
        return inflater.inflate(R.layout.sfa_page2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        subjectId = getSubjectIdFromActivity();

        // 初始化组件
        featuresContainer = view.findViewById(R.id.featuresContainer);
        trashcanItemsContainer = view.findViewById(R.id.trashcanItemsContainer);
        trashcanImage = view.findViewById(R.id.trashcan);
        trashcanContents = new ArrayList<>();
        mainImage = view.findViewById(R.id.mainImage);
        Button submitButton = view.findViewById(R.id.submitButton);

        ImageUtils.loadImageFromAssets(getContext(), mainImage, imageName);

        // 动态生成特征条目
        generateFeatureItems();

        // 设置拖拽功能
        setupDragAndDrop();

        submitButton.setOnClickListener(v -> {
            submitClickCount++;
            hasUserInteracted = true;
            checkFeedback();
        });
    }

    private String getSubjectIdFromActivity() {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            return mainActivity.getActivitySubjectId();
        }
        return "unknown";
    }

    private String getDeviceIdentifier() {
        try {
            String androidId = android.provider.Settings.Secure.getString(
                    requireContext().getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID
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

    private void generateFeatureItems() {
        // 清空现有的特征条目
        featuresContainer.removeAllViews();

        // 动态创建TextView并添加到容器中
        for (Feature feature : selectedFeatures) {
            TextView featureView = createFeatureTextView(feature.getFeatureZh());

            // 使用FlexboxLayout.LayoutParams
            com.google.android.flexbox.FlexboxLayout.LayoutParams params =
                    new com.google.android.flexbox.FlexboxLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
            params.setMargins(8, 6, 8, 6);

            featuresContainer.addView(featureView, params);
        }
    }

    private TextView createFeatureTextView(String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTextSize(36); // 稍微调小字体
        textView.setPadding(3, 3, 3, 3); // 调整内边距
        textView.setTextColor(android.graphics.Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        textView.setMaxLines(6); // 限制最大行数
        textView.setEllipsize(android.text.TextUtils.TruncateAt.END);

        // 设置圆角背景
        android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
        drawable.setColor(android.graphics.Color.parseColor("#FF6750A4"));
        drawable.setCornerRadius(8);
        textView.setBackground(drawable);

        return textView;
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setupDragAndDrop() {
        // 为所有特征条目设置拖动功能
        setupDragForContainer(featuresContainer);

        // 设置垃圾桶的拖放监听器
        trashcanItemsContainer.setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;

                case DragEvent.ACTION_DRAG_ENTERED:
                    trashcanImage.setAlpha(0.5f);
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    trashcanImage.setAlpha(1.0f);
                    return true;

                case DragEvent.ACTION_DROP:
                    hasUserInteracted = true;
                    View droppedView = (View) event.getLocalState();

                    if (droppedView instanceof TextView) {
                        TextView droppedTextView = (TextView) droppedView;

                        if (droppedTextView.getParent() == trashcanItemsContainer) {
                            return true;
                        }

                        if (droppedTextView.getParent() instanceof ViewGroup) {
                            ((ViewGroup) droppedTextView.getParent()).removeView(droppedTextView);
                        }

                        com.google.android.flexbox.FlexboxLayout.LayoutParams params =
                                new com.google.android.flexbox.FlexboxLayout.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                );
                        params.setMargins(6, 4, 6, 4);
                        trashcanItemsContainer.addView(droppedTextView, params);

                        trashcanContents.add(droppedTextView.getText().toString());
                        setupTrashcanItem(droppedTextView);
                    }

                    trashcanImage.setAlpha(1.0f);
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    hasUserInteracted = true;
                    if (!event.getResult()) {
                        View droppedView2 = (View) event.getLocalState();
                        if (droppedView2 instanceof TextView) {
                            TextView droppedTextView = (TextView) droppedView2;
                            if (droppedTextView.getParent() instanceof ViewGroup) {
                                ((ViewGroup) droppedTextView.getParent()).removeView(droppedTextView);
                            }

                            // 使用FlexboxLayout.LayoutParams
                            com.google.android.flexbox.FlexboxLayout.LayoutParams defaultParams =
                                    new com.google.android.flexbox.FlexboxLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                    );
                            defaultParams.setMargins(8, 6, 8, 6);
                            featuresContainer.addView(droppedTextView, defaultParams);
                            trashcanContents.remove(droppedTextView.getText().toString());
                            setupDragForView(droppedTextView);
                        }
                    }

                    trashcanImage.setAlpha(1.0f);
                    return true;
                default:
                    return false;
            }
        });
    }

    private void setupDragForContainer(ViewGroup container) {
        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            setupDragForView(child);
        }
    }

    private void setupDragForView(View view) {
        view.setOnLongClickListener(v -> {
            // 重置背景颜色
            android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
            drawable.setColor(android.graphics.Color.parseColor("#FF6750A4"));
            drawable.setCornerRadius(8);
            v.setBackground(drawable);

            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDragAndDrop(null, shadowBuilder, v, 0);
            return true;
        });
    }

    private void setupTrashcanItem(TextView item) {
        item.setOnLongClickListener(v -> {
            // 重置背景颜色
            android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
            drawable.setColor(android.graphics.Color.parseColor("#FF6750A4"));
            drawable.setCornerRadius(8);
            item.setBackground(drawable);

            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDragAndDrop(null, shadowBuilder, v, 0);
            return true;
        });
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
        if (hasUserInteracted) {
            saveFinalData();
        }
    }

    private void saveFinalData() {
        try {
            // 计算最终总停留时间
            long finalTotalTime = totalTimeOnPage;
            if (lastResumeTime > 0) {
                finalTotalTime += System.currentTimeMillis() - lastResumeTime;
            }

            // 创建数据对象
            Map<String, Object> data = new HashMap<>();
            data.put("imageName", imageName);
            data.put("subjectId", subjectId);
            data.put("experiment", "SFAStep2Part1");
            data.put("totalDuration", finalTotalTime); // 累计停留时间
            data.put("submitClickCount", submitClickCount); // 总点击submit次数

            // 原始特征数据
//            List<String> originalFeatures = new ArrayList<>();
//            for (Feature feature : selectedFeatures) {
//                originalFeatures.add(feature.getFeatureZh());
//            }
//            data.put("originalFeatures", originalFeatures);
//            data.put("correctFeatures", correctFeaturesText);

            // 最终状态
            List<Map<String, Object>> featuresContainerData = collectContainerData(featuresContainer);
            List<Map<String, Object>> trashcanData = collectContainerData(trashcanItemsContainer);
            data.put("finalFeaturesContainer", featuresContainerData);
            data.put("finalTrashcan", trashcanData);

            // 正确性评估
            data.put("correctnessAnalysis", analyzeCorrectness(featuresContainerData, trashcanData));

            // 转换为JSON并保存
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonData = gson.toJson(data);

            saveToFile(jsonData);
//            isDataSaved = true;

            Log.d(TAG, "Final data saved successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error saving final data", e);
        }
    }


    private List<Map<String, Object>> collectContainerData(ViewGroup container) {
        List<Map<String, Object>> containerData = new ArrayList<>();

        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                Map<String, Object> itemData = new HashMap<>();
                itemData.put("text", textView.getText().toString());
                itemData.put("isCorrect", correctFeaturesText.contains(textView.getText().toString()));
                containerData.add(itemData);
            }
        }

        return containerData;
    }

    private Map<String, Object> analyzeCorrectness(List<Map<String, Object>> featuresData, List<Map<String, Object>> trashcanData) {
        Map<String, Object> analysis = new HashMap<>();

        int correctInFeatures = 0;
        int incorrectInFeatures = 0;
        int correctInTrashcan = 0;
        int incorrectInTrashcan = 0;

        // 分析特征容器
        for (Map<String, Object> item : featuresData) {
            boolean isCorrect = (Boolean) item.get("isCorrect");
            if (isCorrect) {
                correctInFeatures++;
            } else {
                incorrectInFeatures++;
            }
        }

        // 分析垃圾桶
        for (Map<String, Object> item : trashcanData) {
            boolean isCorrect = (Boolean) item.get("isCorrect");
            if (isCorrect) {
                incorrectInTrashcan++; // 正确的特征被错误地放入垃圾桶
            } else {
                correctInTrashcan++; // 错误的特征被正确地放入垃圾桶
            }
        }

        analysis.put("correctInFeatures", correctInFeatures);
        analysis.put("incorrectInFeatures", incorrectInFeatures);
        analysis.put("correctInTrashcan", correctInTrashcan);
        analysis.put("incorrectInTrashcan", incorrectInTrashcan);

        boolean allCorrect = (incorrectInFeatures == 0 && incorrectInTrashcan == 0);
        analysis.put("allCorrect", allCorrect);

        return analysis;
    }

    // 保存到文件
    private void saveToFile(String jsonData) {
        try {
            String deviceId = getDeviceIdentifier();
            long timestamp = System.currentTimeMillis();

            // 创建目录结构：deviceId/subjectId/SFA/
            File deviceFolder = new File(requireContext().getExternalFilesDir(null), deviceId);
            File subjectFolder = new File(deviceFolder, subjectId);
            File experimentFolder = new File(subjectFolder, "SFA");

            if (!experimentFolder.exists()) {
                boolean created = experimentFolder.mkdirs();
                if (!created) {
                    Log.e(TAG, "Failed to create folder structure");
                    return;
                }
            }

            // 文件名：timestamp_imageName_step2part1.json
            String fileName = timestamp + "_" + imageName + "_step2part1.json";
            File file = new File(experimentFolder, fileName);

            FileWriter writer = new FileWriter(file);
            writer.write(jsonData);
            writer.close();

            Log.d(TAG, "Data saved to: " + file.getAbsolutePath());

        } catch (IOException e) {
            Log.e(TAG, "Error writing file", e);
        }
    }


    private void checkFeedback() {
        boolean allCorrect = true;

        // 检查垃圾桶内的特性
        for (int i = 0; i < trashcanItemsContainer.getChildCount(); i++) {
            View child = trashcanItemsContainer.getChildAt(i);
            if (child instanceof TextView) {
                TextView featureView = (TextView) child;
                String featureText = featureView.getText().toString();

                android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
                drawable.setCornerRadius(8);

                if (correctFeaturesText.contains(featureText)) {
                    drawable.setColor(android.graphics.Color.parseColor("#F44336")); // 红色 - 错误放入垃圾桶
                    allCorrect = false;
                } else {
                    drawable.setColor(android.graphics.Color.parseColor("#4CAF50")); // 绿色 - 正确放入垃圾桶
                }
                featureView.setBackground(drawable);
            }
        }

        // 检查遗漏在外的特性
        for (int i = 0; i < featuresContainer.getChildCount(); i++) {
            View child = featuresContainer.getChildAt(i);
            if (child instanceof TextView) {
                TextView featureView = (TextView) child;
                String featureText = featureView.getText().toString();

                android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
                drawable.setCornerRadius(8);

                if (correctFeaturesText.contains(featureText)) {
                    drawable.setColor(android.graphics.Color.parseColor("#4CAF50")); // 绿色 - 正确保留
                } else {
                    drawable.setColor(android.graphics.Color.parseColor("#F44336")); // 红色 - 错误保留
                    allCorrect = false;
                }
                featureView.setBackground(drawable);
            }
        }

        if (allCorrect) {
            android.widget.Toast.makeText(getContext(), "全部回答正确！", android.widget.Toast.LENGTH_LONG).show();
        }
    }

}