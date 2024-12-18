package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import java.util.HashMap;
import java.util.Map;

public class SFAPageThreeFragment extends Fragment {

    private GridLayout featuresContainer;

    public SFAPageThreeFragment() {
        // Required empty public constructor
    }

    private final Map<String, String> correctCategories = new HashMap<String, String>() {{
        put("是一种动物", "categoryBox");
        put("是捕食者", "categoryBox");
        put("动作迅速的", "characteristicsBox");
        put("可以吃人", "characteristicsBox");
        put("食肉的", "physicalAttributesBox");
    }};

    @Nullable
    @Override
    public View onCreateView(@NonNull android.view.LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 加载 page_two.xml 布局
        return inflater.inflate(R.layout.sfa_page3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化组件
        featuresContainer = view.findViewById(R.id.featuresContainer);
        LinearLayout functionBox = view.findViewById(R.id.functionBox);
        LinearLayout characteristicsBox = view.findViewById(R.id.characteristicsBox);
        LinearLayout physicalAttributesBox = view.findViewById(R.id.physicalAttributesBox);
        LinearLayout locationBox = view.findViewById(R.id.locationBox);
        LinearLayout categoryBox = view.findViewById(R.id.categoryBox);
        Button submitButton = view.findViewById(R.id.submitButton);

        // 设置拖拽功能
        setupDragAndDrop();

        setDragListener(functionBox);
        setDragListener(characteristicsBox);
        setDragListener(physicalAttributesBox);
        setDragListener(locationBox);
        setDragListener(categoryBox);

        submitButton.setOnClickListener(v -> {
            checkFeedback();
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupDragAndDrop() {
        // 找到所有特征条目
        for (int i = 0; i < featuresContainer.getChildCount(); i++) {
            View featureView = featuresContainer.getChildAt(i);

            // 设置长按监听器，启动拖动
            featureView.setOnLongClickListener(v -> {
                featureView.setBackgroundColor(android.graphics.Color.parseColor("#FF6750A4"));
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                // 启动拖动，并将当前 View 的状态传递给 DragEvent
                v.startDragAndDrop(null, shadowBuilder, v, 0);
                return true;
            });
        }
    }

    private void setDragListener(LinearLayout target) {
        target.setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;

                case DragEvent.ACTION_DRAG_ENTERED:
                    target.setAlpha(0.5f); //
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    target.setAlpha(1.0f); //
                    return true;

                case DragEvent.ACTION_DROP:
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
                        layoutParams.setMargins(5, 5, 5, 5);
                        layoutParams.gravity = Gravity.CENTER;
                        target.addView(droppedTextView, layoutParams);

                        setupTrashcanItem(droppedTextView);
                    }

                    target.setAlpha(1.0f);
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    if (!event.getResult()) {
                        View droppedView2 = (View) event.getLocalState();
                        if (droppedView2 instanceof TextView) {
                            TextView droppedTextView = (TextView) droppedView2;
                            if (droppedTextView.getParent() instanceof ViewGroup) {
                                ((ViewGroup) droppedTextView.getParent()).removeView(droppedTextView);
                            }
                            // 如果条目未被任何容器接收，则还原到 featuresContainer
                            android.widget.GridLayout.LayoutParams defaultParams = new android.widget.GridLayout.LayoutParams();
                            defaultParams.setGravity(Gravity.CENTER);
                            featuresContainer.addView(droppedTextView, defaultParams);
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
            item.setBackgroundColor(android.graphics.Color.parseColor("#FF6750A4"));
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDragAndDrop(null, shadowBuilder, v, 0);
            return true;
        });
    }

    private void checkFeedback() {
        // 1. 遍历所有分类框
        checkCategoryBox("functionBox", R.id.functionBox);
        checkCategoryBox("characteristicsBox", R.id.characteristicsBox);
        checkCategoryBox("physicalAttributesBox", R.id.physicalAttributesBox);
        checkCategoryBox("locationBox", R.id.locationBox);
        checkCategoryBox("categoryBox", R.id.categoryBox);
    }

    private void checkCategoryBox(String categoryName, int boxId) {
        LinearLayout box = getView().findViewById(boxId);

        for (int i = 0; i < box.getChildCount(); i++) {
            View child = box.getChildAt(i);
            if (child instanceof TextView) {
                TextView featureView = (TextView) child;

                // 检查是否是拖入的特性
                if ("feature".equals(featureView.getTag())) {
                    String featureText = featureView.getText().toString();

                    // 检查特性是否在正确的分类中
                    if (correctCategories.containsKey(featureText) &&
                            correctCategories.get(featureText).equals(categoryName)) {
                        featureView.setBackgroundColor(android.graphics.Color.parseColor("#4CAF50")); // 绿色背景
                    } else {
                        featureView.setBackgroundColor(android.graphics.Color.parseColor("#F44336")); // 红色背景
                    }
                }
            }
        }
    }
}