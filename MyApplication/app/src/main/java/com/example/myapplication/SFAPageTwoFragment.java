package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

public class SFAPageTwoFragment extends Fragment {

    private LinearLayout featuresContainer;       // 特征条目容器
    private GridLayout trashcanItemsContainer; // 垃圾桶条目容器
    private ImageView trashcanImage;             // 垃圾桶图片
    private List<String> trashcanContents;       // 用于记录垃圾桶的内容

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

        // 设置拖拽功能
        setupDragAndDrop();
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

        // 设置垃圾桶的拖放监听器
        trashcanItemsContainer.setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;

                case DragEvent.ACTION_DRAG_ENTERED:
                    // 拖动进入垃圾桶
                    trashcanImage.setAlpha(0.5f); // 高亮垃圾桶
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    // 拖动离开垃圾桶
                    trashcanImage.setAlpha(1.0f); // 恢复垃圾桶透明度
                    return true;

                case DragEvent.ACTION_DROP:
                    // 获取被拖动的条目
                    View droppedView = (View) event.getLocalState();

                    if (droppedView instanceof TextView) {
                        TextView droppedTextView = (TextView) droppedView;

                        if (droppedTextView.getParent() == trashcanItemsContainer) {
                            return true;
                        }

                        // 检查并从原父容器中移除
                        if (droppedTextView.getParent() instanceof ViewGroup) {
                            ((ViewGroup) droppedTextView.getParent()).removeView(droppedTextView);
                        }

                        // 将条目添加到垃圾桶中的 GridLayout
                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                        params.setMargins(16, 16, 16, 16); // 设置条目之间间距
                        trashcanItemsContainer.addView(droppedTextView, params);

                        trashcanContents.add(droppedTextView.getText().toString());

                        // 设置垃圾桶条目的拖动功能
                        setupTrashcanItem(droppedTextView);
                    }

                    trashcanImage.setAlpha(1.0f); // 恢复垃圾桶透明度
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
                            trashcanContents.remove(droppedTextView.getText().toString());
                        }
                    }

                    trashcanImage.setAlpha(1.0f);
                    return true;
                default:
                    return false;
            }
        });
    }

    // 为垃圾桶条目设置拖动功能
    private void setupTrashcanItem(TextView item) {
        item.setOnLongClickListener(v -> {
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDragAndDrop(null, shadowBuilder, v, 0);
            return true;
        });
    }
}