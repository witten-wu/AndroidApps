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
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SFAPageThreeFragment extends Fragment {

    private static final String ARG_CORRECT_FEATURES = "correct_features";
    private static final String ARG_IMAGE_NAME = "image_name";

    private FlexboxLayout featuresContainer;
    private ImageView mainImage;
    private List<String> correctFeatures;
    private String imageName;
    private String subjectId;
    private static final String TAG = "SFAPageThreeFragment";
    private long totalTimeOnPage = 0; // 累计停留时间
    private long lastResumeTime; // 最后一次resume的时间
    private int submitClickCount = 0;
    private boolean hasUserInteracted = false;

    public SFAPageThreeFragment() {
        // Required empty public constructor
    }

//    private final Map<String, String> correctCategories = new HashMap<String, String>() {{
//        put("是一种动物", "categoryBox");
//        put("是捕食者", "categoryBox");
//        put("动作迅速的", "characteristicsBox");
//        put("可以吃人", "characteristicsBox");
//        put("食肉的", "physicalAttributesBox");
//    }};

    public static SFAPageThreeFragment newInstance(List<String> correctFeatures, String imageName) {
        SFAPageThreeFragment fragment = new SFAPageThreeFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_CORRECT_FEATURES, new ArrayList<>(correctFeatures));
        args.putString(ARG_IMAGE_NAME, imageName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            correctFeatures = getArguments().getStringArrayList(ARG_CORRECT_FEATURES);
            imageName = getArguments().getString(ARG_IMAGE_NAME);
        }

        hasUserInteracted = false;
        submitClickCount = 0;
        totalTimeOnPage = 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull android.view.LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sfa_page3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        subjectId = getSubjectIdFromActivity();

        // 初始化组件
        mainImage = view.findViewById(R.id.mainImage);
        featuresContainer = view.findViewById(R.id.featuresContainer);
        LinearLayout functionBox = view.findViewById(R.id.functionBox);
        LinearLayout characteristicsBox = view.findViewById(R.id.characteristicsBox);
        LinearLayout physicalAttributesBox = view.findViewById(R.id.physicalAttributesBox);
        LinearLayout locationBox = view.findViewById(R.id.locationBox);
        LinearLayout categoryBox = view.findViewById(R.id.categoryBox);
        Button submitButton = view.findViewById(R.id.submitButton);

        ImageUtils.loadImageFromAssets(getContext(), mainImage, imageName);

        // 生成特征条目
        generateFeatureItems();

        // 设置拖拽功能
        setupDragAndDrop();

        setDragListener(functionBox);
        setDragListener(characteristicsBox);
        setDragListener(physicalAttributesBox);
        setDragListener(locationBox);
        setDragListener(categoryBox);

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
        featuresContainer.removeAllViews();

        if (correctFeatures != null) {
            for (String feature : correctFeatures) {
                TextView featureView = createFeatureTextView(feature);

                FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(6, 6, 6, 6);

                featuresContainer.addView(featureView, params);
            }
        }
    }

    private TextView createFeatureTextView(String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTextSize(30);
        textView.setPadding(2, 0, 2, 0);
        textView.setTextColor(android.graphics.Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        textView.setMaxLines(4);
        textView.setEllipsize(android.text.TextUtils.TruncateAt.END);

        android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
        drawable.setColor(android.graphics.Color.parseColor("#FF6750A4"));
        drawable.setCornerRadius(8);
        textView.setBackground(drawable);

        return textView;
    }


    private void setupDragAndDrop() {
        for (int i = 0; i < featuresContainer.getChildCount(); i++) {
            View featureView = featuresContainer.getChildAt(i);
            setupDragForView(featureView);
        }
    }

    private void setupDragForView(View view) {
        view.setOnLongClickListener(v -> {
            android.graphics.drawable.GradientDrawable drawable = new android.graphics.drawable.GradientDrawable();
            drawable.setColor(android.graphics.Color.parseColor("#FF6750A4"));
            drawable.setCornerRadius(8);
            v.setBackground(drawable);

            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDragAndDrop(null, shadowBuilder, v, 0);
            return true;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setDragListener(LinearLayout target) {
        target.setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;

                case DragEvent.ACTION_DRAG_ENTERED:
                    target.setAlpha(0.5f);
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    target.setAlpha(1.0f);
                    return true;

                case DragEvent.ACTION_DROP:
                    hasUserInteracted = true;
                    View droppedView = (View) event.getLocalState();

                    if (droppedView instanceof TextView) {
                        TextView droppedTextView = (TextView) droppedView;

                        if (droppedTextView.getParent() == target) {
                            return true;
                        }

                        if (droppedTextView.getParent() instanceof ViewGroup) {
                            ((ViewGroup) droppedTextView.getParent()).removeView(droppedTextView);
                        }

                        droppedTextView.setTag("feature");

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        layoutParams.setMargins(3, 3, 3, 3);
                        layoutParams.gravity = Gravity.CENTER;
                        target.addView(droppedTextView, layoutParams);

                        setupTrashcanItem(droppedTextView);
                    }

                    target.setAlpha(1.0f);
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

                            FlexboxLayout.LayoutParams defaultParams = new FlexboxLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            );
                            defaultParams.setMargins(6, 6, 6, 6);
                            featuresContainer.addView(droppedTextView, defaultParams);
                            setupDragForView(droppedTextView);
                        }
                    }
                    target.setAlpha(1.0f);
                    return true;
                default:
                    return false;
            }
        });
    }

    private void setupTrashcanItem(TextView item) {
        item.setOnLongClickListener(v -> {
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
            data.put("experiment", "SFAStep2Part2");
            data.put("totalDuration", finalTotalTime); // 累计停留时间
            data.put("submitClickCount", submitClickCount); // 总点击submit次数

            data.put("finalFeaturesContainer", collectFeaturesContainerTexts(featuresContainer));

            // 最终状态
            View rootView = getView();
            if (rootView != null) {
                data.put("functionBox", collectCategoryBoxTexts(rootView.findViewById(R.id.functionBox)));
                data.put("characteristicsBox", collectCategoryBoxTexts(rootView.findViewById(R.id.characteristicsBox)));
                data.put("physicalAttributesBox", collectCategoryBoxTexts(rootView.findViewById(R.id.physicalAttributesBox)));
                data.put("locationBox", collectCategoryBoxTexts(rootView.findViewById(R.id.locationBox)));
                data.put("categoryBox", collectCategoryBoxTexts(rootView.findViewById(R.id.categoryBox)));
            }

            // 正确性评估
            //data.put("correctnessAnalysis", analyzeCorrectness(featuresContainerData, trashcanData));

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

    private List<String> collectFeaturesContainerTexts(ViewGroup container) {
        List<String> containerTexts = new ArrayList<>();

        if (container == null) {
            return containerTexts;
        }

        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                containerTexts.add(textView.getText().toString());
            }
        }

        return containerTexts;
    }

    private List<String> collectCategoryBoxTexts(ViewGroup container) {
        List<String> containerTexts = new ArrayList<>();

        if (container == null) {
            return containerTexts;
        }

        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            if (child instanceof TextView) {
                TextView textView = (TextView) child;

                // 只收集拖拽进来的特征项（有"feature"标签的）
                if ("feature".equals(textView.getTag())) {
                    containerTexts.add(textView.getText().toString());
                }
            }
        }

        return containerTexts;
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
            String fileName = timestamp + "_" + imageName + "_step2part2.json";
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
        boolean allCorrect = true; // 用于标记是否全部正确

        // 遍历所有分类框
        allCorrect &= checkCategoryBox("functionBox", R.id.functionBox);
        allCorrect &= checkCategoryBox("characteristicsBox", R.id.characteristicsBox);
        allCorrect &= checkCategoryBox("physicalAttributesBox", R.id.physicalAttributesBox);
        allCorrect &= checkCategoryBox("locationBox", R.id.locationBox);
        allCorrect &= checkCategoryBox("categoryBox", R.id.categoryBox);

        if (allCorrect) {
            android.widget.Toast.makeText(getContext(), "回答正确！", android.widget.Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkCategoryBox(String categoryName, int boxId) {
//        boolean isCorrect = true;
//        LinearLayout box = getView().findViewById(boxId);
//
//        for (int i = 0; i < box.getChildCount(); i++) {
//            View child = box.getChildAt(i);
//            if (child instanceof TextView) {
//                TextView featureView = (TextView) child;
//
//                // 检查是否是拖入的特性
//                if ("feature".equals(featureView.getTag())) {
//                    String featureText = featureView.getText().toString();
//
//                    // 检查特性是否在正确的分类中
//                    if (correctCategories.containsKey(featureText) &&
//                            correctCategories.get(featureText).equals(categoryName)) {
//                        featureView.setBackgroundColor(android.graphics.Color.parseColor("#4CAF50")); // 绿色背景
//                    } else {
//                        featureView.setBackgroundColor(android.graphics.Color.parseColor("#F44336")); // 红色背景
//                        isCorrect = false;
//                    }
//                }
//            }
//        }
//
//        return isCorrect;
        return false;
    }
}