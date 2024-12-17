package com.example.myapplication;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VNESTPageTwo extends Fragment {

    private LineView lineView; // 自定义视图，用于绘制连线
    private List<Pair<View, View>> userConnections = new ArrayList<>(); // 存储用户连线
    private View selectedStart = null; // 当前选中的起点
    private float[] startCoords = new float[2]; // 起点坐标

    // 定义正确答案
    private final List<Pair<String, String>> correctConnections = Arrays.asList(
            new Pair<>("工人", "板车"),
            new Pair<>("旅客", "行李")
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 加载布局
        View rootView = inflater.inflate(R.layout.vnest_page2, container, false);

        // 找到 LineView
        lineView = rootView.findViewById(R.id.lineView);

        // 收集第一个 Box 和第三个 Box 的选项
        List<View> box1Options = new ArrayList<>();
        box1Options.add(rootView.findViewById(R.id.option1_1));
        box1Options.add(rootView.findViewById(R.id.option1_2));
        box1Options.add(rootView.findViewById(R.id.option1_3));
        box1Options.add(rootView.findViewById(R.id.option1_4));

        List<View> box3Options = new ArrayList<>();
        box3Options.add(rootView.findViewById(R.id.option3_1));
        box3Options.add(rootView.findViewById(R.id.option3_2));
        box3Options.add(rootView.findViewById(R.id.option3_3));
        box3Options.add(rootView.findViewById(R.id.option3_4));

        // 为第一个 Box 的选项添加手势监听
        setupTouchListeners(box1Options, box3Options);

        // 设置提交按钮逻辑
        Button submitButton = rootView.findViewById(R.id.submitButton);
        setupSubmitButton(submitButton, box1Options, box3Options);

        // 设置清空按钮逻辑
        Button clearButton = rootView.findViewById(R.id.clearButton);
        setupClearButton(clearButton);

        return rootView;
    }

    // 为选项设置触摸监听
    private void setupTouchListeners(List<View> startOptions, List<View> endOptions) {
        for (View startOption : startOptions) {
            startOption.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 禁止父布局拦截事件
                        v.getParent().requestDisallowInterceptTouchEvent(true);

                        // 按下时记录起点
                        selectedStart = v;
                        startCoords = getViewCenter((TextView) selectedStart); // 获取文字的中心坐标
                        lineView.startDrawing(startCoords[0], startCoords[1]); // 使用文字中心作为起点
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        // 动态更新连线的终点
                        lineView.updateLine(event.getRawX(), event.getRawY());
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // 允许父布局拦截事件
                        v.getParent().requestDisallowInterceptTouchEvent(false);

                        // 停止动态绘制
                        lineView.stopDrawing();

                        // 检查终点是否有效
                        View endOption = findTargetOption(endOptions, event.getRawX(), event.getRawY());
                        if (endOption != null && !isConnectionExists(selectedStart, endOption)) {
                            if (userConnections.size() < 2) {
                                // 获取起点的右侧坐标和终点的左侧坐标
                                float[] startCoords = getTextSideCoordinates((TextView) selectedStart, false); // 起点右侧
                                float[] endCoords = getTextSideCoordinates((TextView) endOption, true); // 终点左侧

                                // 添加连线
                                lineView.addLine(
                                        startCoords[0], startCoords[1],
                                        endCoords[0], endCoords[1]
                                );

                                // 记录连线
                                userConnections.add(new Pair<>(selectedStart, endOption));
                            } else {
                                Toast.makeText(getContext(), "最多只能连两条线！", Toast.LENGTH_SHORT).show();
                            }
                        }
                        selectedStart = null;

                        // 调用 performClick，确保无障碍服务兼容
                        v.performClick();
                        return true;
                }
                return false;
            });
        }
    }

    // 提交按钮逻辑
    private void setupSubmitButton(Button submitButton, List<View> startOptions, List<View> endOptions) {
        submitButton.setOnClickListener(v -> {
            // 清空当前连线（重新绘制）
            lineView.clearLines();

            // 判断用户连线是否正确并重新绘制
            for (Pair<View, View> connection : userConnections) {
                String startText = ((TextView) connection.first).getText().toString();
                String endText = ((TextView) connection.second).getText().toString();

                if (correctConnections.contains(new Pair<>(startText, endText))) {
                    // 正确连线 -> 绿色
                    lineView.addLine(
                            getTextSideCoordinates((TextView) connection.first, false)[0], // 起点右侧
                            getTextSideCoordinates((TextView) connection.first, false)[1],
                            getTextSideCoordinates((TextView) connection.second, true)[0], // 终点左侧
                            getTextSideCoordinates((TextView) connection.second, true)[1],
                            Color.GREEN
                    );
                } else {
                    // 错误连线 -> 红色
                    lineView.addLine(
                            getTextSideCoordinates((TextView) connection.first, false)[0], // 起点右侧
                            getTextSideCoordinates((TextView) connection.first, false)[1],
                            getTextSideCoordinates((TextView) connection.second, true)[0], // 终点左侧
                            getTextSideCoordinates((TextView) connection.second, true)[1]
                    );
                }
            }

            // 绘制正确答案
            for (Pair<String, String> correct : correctConnections) {
                View startView = findViewByText(startOptions, correct.first);
                View endView = findViewByText(endOptions, correct.second);

                if (startView != null && endView != null && !isConnectionExists(startView, endView)) {
                    // 绘制正确答案（绿色）
                    lineView.addLine(
                            getTextSideCoordinates((TextView) startView, false)[0], // 起点右侧
                            getTextSideCoordinates((TextView) startView, false)[1],
                            getTextSideCoordinates((TextView) endView, true)[0], // 终点左侧
                            getTextSideCoordinates((TextView) endView, true)[1],
                            Color.GREEN
                    );
                }
            }
        });
    }

    // 检查是否已经存在连线
    private boolean isConnectionExists(View start, View end) {
        for (Pair<View, View> connection : userConnections) {
            if (connection.first == start && connection.second == end) {
                return true;
            }
        }
        return false;
    }

    private float[] getTextSideCoordinates(TextView textView, boolean isLeftSide) {
        // 获取 TextView 的屏幕位置
        int[] location = new int[2];
        textView.getLocationOnScreen(location);

        // 获取文字的布局信息
        Layout layout = textView.getLayout();
        if (layout == null) {
            // 如果 Layout 不存在，直接返回 TextView 的边缘作为坐标
            if (isLeftSide) {
                return new float[]{location[0], location[1] + textView.getHeight() / 2f};
            } else {
                return new float[]{location[0] + textView.getWidth(), location[1] + textView.getHeight() / 2f};
            }
        }

        // 文字的基线、上升量和下降量
        float baseline = layout.getLineBaseline(0);
        float ascent = layout.getLineAscent(0);
        float descent = layout.getLineDescent(0);

        // 计算文字的实际高度
        float textHeight = -ascent + descent;

        // 修正纵向中心
        float textCenterY = location[1] + baseline - textHeight / 2f;

        // 根据是否为左侧或右侧返回不同的横坐标
        if (isLeftSide) {
            // 左侧坐标
            return new float[]{location[0], textCenterY};
        } else {
            // 右侧坐标
            return new float[]{location[0] + textView.getWidth(), textCenterY};
        }
    }

    // 获取 View 的中心点坐标
    private float[] getViewCenter(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location); // 获取 View 的屏幕位置
        float centerX = location[0] + view.getWidth() / 2f; // 中心 X 坐标
        float centerY = location[1] + view.getHeight() / 2f; // 中心 Y 坐标
        return new float[]{centerX, centerY};
    }

    private void setupClearButton(Button clearButton) {
        clearButton.setOnClickListener(v -> {
            // 清空所有连线
            lineView.clearLines();
            // 清空用户记录的连线
            userConnections.clear();
        });
    }

    private float[] getTextCenter(TextView textView) {
        // 获取 TextView 的屏幕位置
        int[] location = new int[2];
        textView.getLocationOnScreen(location);

        // 获取 TextView 的宽度和高度
        int textViewWidth = textView.getWidth();
        int textViewHeight = textView.getHeight();

        // 获取文字的布局
        Layout layout = textView.getLayout();
        if (layout == null) {
            // 如果 Layout 不存在，返回 TextView 的几何中心
            return new float[]{
                    location[0] + textViewWidth / 2f,
                    location[1] + textViewHeight / 2f
            };
        }

        // 获取第一行文字的基线、上升量和下降量
        float baseline = layout.getLineBaseline(0);
        float ascent = layout.getLineAscent(0);
        float descent = layout.getLineDescent(0);

        // 计算文字的实际高度
        float textHeight = -ascent + descent;

        // 修正横向中心
        float textCenterX = location[0] + textViewWidth / 2f;

        // 修正纵向中心
        // location[1] 是 TextView 顶部的屏幕 Y 坐标
        // baseline 是相对于 TextView 顶部的偏移
        // 文字的中心需要从 baseline 上移一半文字高度
        float textCenterY = location[1] + baseline - textHeight / 2f;

        return new float[]{textCenterX, textCenterY};
    }


    // 根据触摸点找到目标选项
    private View findTargetOption(List<View> options, float x, float y) {
        for (View option : options) {
            int[] location = new int[2];
            option.getLocationOnScreen(location); // 获取选项的屏幕位置

            // 检查触摸点是否在选项范围内
            if (x >= location[0] && x <= location[0] + option.getWidth() &&
                    y >= location[1] && y <= location[1] + option.getHeight()) {
                return option;
            }
        }
        return null; // 未找到匹配的终点选项
    }

    // 根据文字查找 View
    private View findViewByText(List<View> options, String text) {
        for (View option : options) {
            if (option instanceof TextView && ((TextView) option).getText().toString().equals(text)) {
                return option;
            }
        }
        return null;
    }
}