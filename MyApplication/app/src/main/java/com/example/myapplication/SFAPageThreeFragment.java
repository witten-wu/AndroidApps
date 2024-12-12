package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SFAPageThreeFragment extends Fragment {

    private LinearLayout featuresContainer;

    public SFAPageThreeFragment() {
        // Required empty public constructor
    }

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

        // 设置拖拽功能
        setupDragAndDrop();

        setDragListener(functionBox);
        setDragListener(characteristicsBox);
        setDragListener(physicalAttributesBox);
        setDragListener(locationBox);
        setDragListener(categoryBox);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupDragAndDrop() {
        // 找到所有特征条目
        for (int i = 0; i < featuresContainer.getChildCount(); i++) {
            View featureView = featuresContainer.getChildAt(i);

            // 设置长按监听器，启动拖动
            featureView.setOnLongClickListener(v -> {
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

                        target.addView(droppedTextView);

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
                            featuresContainer.addView(droppedTextView);
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
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDragAndDrop(null, shadowBuilder, v, 0);
            return true;
        });
    }
}