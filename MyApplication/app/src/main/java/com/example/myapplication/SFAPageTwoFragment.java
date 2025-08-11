package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import android.widget.GridLayout;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

public class SFAPageTwoFragment extends Fragment {
    private static final String ARG_IMAGE_NAME = "image_name";
    private String imageName;
    private FlexboxLayout featuresContainer;       // 特征条目容器
    //private android.widget.GridLayout trashcanItemsContainer; // 垃圾桶条目容器

    private FlexboxLayout trashcanItemsContainer; // 垃圾桶条目容器
    private ImageView trashcanImage;             // 垃圾桶图片
    private ImageView mainImage;
    private List<String> trashcanContents;       // 用于记录垃圾桶的内容
    private List<Feature> selectedFeatures;
    private List<String> correctFeaturesText;

    public static SFAPageTwoFragment newInstance(String imageName) {
        SFAPageTwoFragment fragment = new SFAPageTwoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_NAME, imageName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageName = getArguments().getString(ARG_IMAGE_NAME);
        }
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

        // 初始化组件
        featuresContainer = view.findViewById(R.id.featuresContainer);
        trashcanItemsContainer = view.findViewById(R.id.trashcanItemsContainer);
        trashcanImage = view.findViewById(R.id.trashcan);
        trashcanContents = new ArrayList<>();
        mainImage = view.findViewById(R.id.mainImage);
        Button submitButton = view.findViewById(R.id.submitButton);

        loadWordData();

        // 设置拖拽功能
        setupDragAndDrop();

        submitButton.setOnClickListener(v -> {
            checkFeedback();
        });
    }

    private void loadWordData() {
        // 从CSV文件读取特征数据
        String csvFileName = "treatment_organized.csv"; // 改为CSV文件
        List<Feature> allFeatures = ExcelReader.getFeaturesForWord(getContext(), csvFileName, imageName);

        // 选择8个正确特征和8个错误特征
        selectedFeatures = ExcelReader.selectFeatures(allFeatures, 8, 8);

        // 提取正确特征的文本用于后续验证
        correctFeaturesText = new ArrayList<>();
        for (Feature feature : selectedFeatures) {
            if (feature.hasFeature()) {
                correctFeaturesText.add(feature.getFeatureZh());
            }
        }

        // 加载对应的图片
        ImageUtils.loadImageFromAssets(getContext(), mainImage, imageName);

        // 动态生成特征条目
        generateFeatureItems();
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
            params.setMargins(6, 6, 6, 6);

            featuresContainer.addView(featureView, params);
        }
    }

    private TextView createFeatureTextView(String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTextSize(23); // 稍微调小字体
        textView.setPadding(3, 3, 3, 3); // 调整内边距
        textView.setTextColor(android.graphics.Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        textView.setMaxLines(4); // 限制最大行数
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
                        params.setMargins(3, 3, 3, 3);
                        trashcanItemsContainer.addView(droppedTextView, params);

                        trashcanContents.add(droppedTextView.getText().toString());
                        setupTrashcanItem(droppedTextView);
                    }

                    trashcanImage.setAlpha(1.0f);
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
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
                            defaultParams.setMargins(6, 6, 6, 6);
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
            android.widget.Toast.makeText(getContext(), "恭喜你全部回答正确！", android.widget.Toast.LENGTH_LONG).show();
//            navigateToPageThree();
        }
    }

//    private void navigateToPageThree() {
//        SFAPageThreeFragment pageThreeFragment = SFAPageThreeFragment.newInstance(
//                correctFeaturesText, // 传递正确的特征
//                imageName            // 传递图片名称
//        );
//
//        getParentFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragment_container, pageThreeFragment)
//                .addToBackStack(null)
//                .commit();
//    }
}