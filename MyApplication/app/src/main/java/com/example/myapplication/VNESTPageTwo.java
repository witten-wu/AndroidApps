package com.example.myapplication;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.CompoundButtonCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class VNESTPageTwo extends Fragment {

    private LineView lineView; // 自定义视图，用于绘制连线
    private List<Pair<CheckBox, CheckBox>> userConnections = new ArrayList<>(); // 存储用户连线
    private CheckBox selectedStart = null; // 当前选中的起点

    private CheckBox selectedEnd = null; // 当前选中的终点
//    private float[] startCoords = new float[2]; // 起点坐标

    private boolean isSelectingStart = true; // 当前是否在选择起点
    private final int maxConnections = 2; // 最大连线数量

    // 定义正确答案
    private final List<Pair<String, String>> correctConnections = Arrays.asList(
            new Pair<>("工人", "板车"),
            new Pair<>("旅客", "行李")
    );

    // 定义第一列的正确选项
    private final List<String> correctFirstColumnOptions = Arrays.asList("工人", "旅客");

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

        List<View> checkbox1Options = new ArrayList<>();
        checkbox1Options.add(rootView.findViewById(R.id.checkbox_option1_1));
        checkbox1Options.add(rootView.findViewById(R.id.checkbox_option1_2));
        checkbox1Options.add(rootView.findViewById(R.id.checkbox_option1_3));
        checkbox1Options.add(rootView.findViewById(R.id.checkbox_option1_4));

        List<View> checkbox3Options = new ArrayList<>();
        checkbox3Options.add(rootView.findViewById(R.id.checkbox_option3_1));
        checkbox3Options.add(rootView.findViewById(R.id.checkbox_option3_2));
        checkbox3Options.add(rootView.findViewById(R.id.checkbox_option3_3));
        checkbox3Options.add(rootView.findViewById(R.id.checkbox_option3_4));

        Map<View, View> checkboxToOptionMap = new HashMap<>();
        checkboxToOptionMap.put(rootView.findViewById(R.id.checkbox_option1_1), rootView.findViewById(R.id.option1_1));
        checkboxToOptionMap.put(rootView.findViewById(R.id.checkbox_option1_2), rootView.findViewById(R.id.option1_2));
        checkboxToOptionMap.put(rootView.findViewById(R.id.checkbox_option1_3), rootView.findViewById(R.id.option1_3));
        checkboxToOptionMap.put(rootView.findViewById(R.id.checkbox_option1_4), rootView.findViewById(R.id.option1_4));
        checkboxToOptionMap.put(rootView.findViewById(R.id.checkbox_option3_1), rootView.findViewById(R.id.option3_1));
        checkboxToOptionMap.put(rootView.findViewById(R.id.checkbox_option3_2), rootView.findViewById(R.id.option3_2));
        checkboxToOptionMap.put(rootView.findViewById(R.id.checkbox_option3_3), rootView.findViewById(R.id.option3_3));
        checkboxToOptionMap.put(rootView.findViewById(R.id.checkbox_option3_4), rootView.findViewById(R.id.option3_4));

        clearCheckBoxes(checkbox1Options);
        clearCheckBoxes(checkbox3Options);

        // 为第一个 Box 的选项添加手势监听
        setupClickListeners(checkbox1Options, checkbox3Options, checkboxToOptionMap);

        // 设置提交按钮逻辑
        Button submitButton = rootView.findViewById(R.id.submitButton);
        setupSubmitButton(submitButton, box1Options, box3Options, checkbox1Options, checkbox3Options, checkboxToOptionMap);

        // 设置清空按钮逻辑
        Button clearButton = rootView.findViewById(R.id.clearButton);
        setupClearButton(clearButton, checkbox1Options, checkbox3Options);

        return rootView;
    }

    private void setupClickListeners(List<View> box1Options, List<View> box3Options, Map<View, View> checkboxToOptionMap) {
        // 第一个 Box 的 CheckBox 监听
        for (View checkbox : box1Options) {
            checkbox.setOnClickListener(v -> {
                CheckBox checkBox = (CheckBox) checkbox;
                boolean wasChecked = checkBox.isChecked(); // 当前点击后的状态
                View associatedOption = checkboxToOptionMap.get(checkbox);

                // 获取当前选中的文本内容
                String selectedText = null;
                if (associatedOption instanceof TextView) {
                    selectedText = ((TextView) associatedOption).getText().toString();
                }

                // 判断是否为正确选项
                if (selectedText != null && correctFirstColumnOptions.contains(selectedText)) {
                    // 正确选项：设置为绿色
                    CompoundButtonCompat.setButtonTintList(checkBox, ColorStateList.valueOf(Color.GREEN));
                } else {
                    // 错误选项：设置为红色
                    CompoundButtonCompat.setButtonTintList(checkBox, ColorStateList.valueOf(Color.parseColor("#FFCCCB")));
                }

                // 如果选中的 CheckBox 已连线，阻止取消
                if (isAlreadyConnected(checkBox)) {
                    checkBox.setChecked(true);
                    return;
                }

                int box1CheckedCount = getCheckedCount(box1Options) - (wasChecked ? 1 : 0); // 如果已勾选，减去当前
                int box3CheckedCount = getCheckedCount(box3Options);

                // 如果取消勾选，重置状态
                if (!wasChecked) {
                    CompoundButtonCompat.setButtonTintList(checkBox, ColorStateList.valueOf(Color.BLACK));
                    if (selectedStart == checkBox) {
                        selectedStart = null;
                        isSelectingStart = true; // 重置为起点选择模式
                    }
                    return;
                }

                // 只有当 Box1 的已勾选数量等于 Box3 的已勾选数量时，才能继续勾选 Box1
                if (box1CheckedCount == box3CheckedCount) {
                    if (isSelectingStart && !isAlreadyConnected(checkBox)) {
                        // 设置为起点
                        selectedStart = checkBox;
                        isSelectingStart = false; // 切换到选择终点模式
                    }
                } else {
                    // 不满足条件，取消勾选
                    checkBox.setChecked(false);
                    CompoundButtonCompat.setButtonTintList(checkBox, ColorStateList.valueOf(Color.BLACK));
                    Toast.makeText(getContext(), "请选择'什么'", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // 第三个 Box 的 CheckBox 监听（保持不变）
        for (View checkbox : box3Options) {
            checkbox.setOnClickListener(v -> {
                CheckBox checkBox = (CheckBox) checkbox;
                boolean wasChecked = checkBox.isChecked(); // 当前点击后的状态
                View associatedOption = checkboxToOptionMap.get(checkbox);

                // 如果选中的 CheckBox 已连线，阻止取消
                if (isAlreadyConnected(checkBox)) {
                    checkBox.setChecked(true);
                    return;
                }

                int box1CheckedCount = getCheckedCount(box1Options);
                int box3CheckedCount = getCheckedCount(box3Options) - (wasChecked ? 1 : 0); // 如果已勾选，减去当前

                if (box1CheckedCount == box3CheckedCount + 1) {
                    if (!isSelectingStart && !isAlreadyConnected(checkBox)) {
                        // 设置为终点
                        selectedEnd = checkBox;

                        // 检查连线数量是否已达到最大值
                        if (userConnections.size() < maxConnections) {
                            // 自动连线
                            if (selectedStart != null && selectedEnd != null) {
                                createConnection(selectedStart, selectedEnd); // 使用 TextView 进行连线
                                selectedStart = null;
                                selectedEnd = null;
                                isSelectingStart = true; // 切换回选择起点
                            }
                        } else {
                            // 超过最大连线数，取消当前选中状态
                            checkBox.setChecked(false); // 取消终点的勾选

                            CompoundButtonCompat.setButtonTintList(checkBox, ColorStateList.valueOf(Color.BLACK));
                            if (selectedStart instanceof CheckBox) {
                                ((CheckBox) selectedStart).setChecked(false); // 取消起点的勾选
                                CompoundButtonCompat.setButtonTintList(((CheckBox) selectedStart), ColorStateList.valueOf(Color.BLACK));
                            }

                            // 提示用户
                            Toast.makeText(getContext(), "最多只能连两条线！", Toast.LENGTH_SHORT).show();

                            // 重置选择状态
                            selectedStart = null;
                            selectedEnd = null;
                            isSelectingStart = true; // 切换回选择起点
                        }
                    }
                } else {
                    // 不满足条件，取消勾选
                    checkBox.setChecked(false);
                    CompoundButtonCompat.setButtonTintList(checkBox, ColorStateList.valueOf(Color.BLACK));
                    Toast.makeText(getContext(), "请选择'谁'", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean isAlreadyConnected(View option) {
        for (Pair<CheckBox, CheckBox> connection : userConnections) {
            if (connection.first == option || connection.second == option) {
                return true;
            }
        }
        return false;
    }

    // 创建连线
    private void createConnection(CheckBox start, CheckBox end) {
        createConnection(start, end, Color.BLACK); // 调用带颜色参数的方法
    }

    private void createConnection(CheckBox start, CheckBox end, int color) {
        float[] startCoords = getCheckBoxCenterCoordinates(start);
        float[] endCoords = getCheckBoxCenterCoordinates(end);

        // 在 LineView 上绘制连线（指定颜色）
        lineView.addLine(startCoords[0], startCoords[1], endCoords[0], endCoords[1], color);

        // 保存用户连线
        userConnections.add(new Pair<>(start, end));
    }

    private void createConnection(CheckBox start, CheckBox end, int color, boolean addToUserConnections) {
        float[] startCoords = getCheckBoxCenterCoordinates(start);
        float[] endCoords = getCheckBoxCenterCoordinates(end);

        // 在 LineView 上绘制连线（指定颜色）
        lineView.addLine(startCoords[0], startCoords[1], endCoords[0], endCoords[1], color);

        // 如果需要，添加到 userConnections
        if (addToUserConnections) {
            userConnections.add(new Pair<>(start, end));
        }
    }

//    // 为选项设置触摸监听
//    private void setupTouchListeners(List<View> startOptions, List<View> endOptions) {
//        for (View startOption : startOptions) {
//            startOption.setOnTouchListener((v, event) -> {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        // 禁止父布局拦截事件
//                        v.getParent().requestDisallowInterceptTouchEvent(true);
//
//                        // 按下时记录起点
//                        selectedStart = v;
//                        startCoords = getTextSideCoordinates((TextView) selectedStart, true);
//                        lineView.startDrawing(startCoords[0], startCoords[1]);
//                        return true;
//
//                    case MotionEvent.ACTION_MOVE:
//                        // 动态更新连线的终点
//                        lineView.updateLine(event.getRawX(), event.getRawY());
//                        return true;
//
//                    case MotionEvent.ACTION_UP:
//                    case MotionEvent.ACTION_CANCEL:
//                        // 允许父布局拦截事件
//                        v.getParent().requestDisallowInterceptTouchEvent(false);
//
//                        // 停止动态绘制
//                        lineView.stopDrawing();
//
//                        // 检查终点是否有效
//                        View endOption = findTargetOption(endOptions, event.getRawX(), event.getRawY());
//                        if (endOption != null && !isConnectionExists(selectedStart, endOption)) {
//                            if (userConnections.size() < 2) {
//                                // 获取起点的右侧坐标和终点的左侧坐标
//                                float[] startCoords = getTextSideCoordinates((TextView) selectedStart, true); // 起点左侧
//                                float[] endCoords = getTextSideCoordinates((TextView) endOption, true); // 终点左侧
//
//                                // 添加连线
//                                lineView.addLine(
//                                        startCoords[0], startCoords[1],
//                                        endCoords[0], endCoords[1]
//                                );
//
//                                // 记录连线
//                                userConnections.add(new Pair<>(selectedStart, endOption));
//                            } else {
//                                Toast.makeText(getContext(), "最多只能连两条线！", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        selectedStart = null;
//
//                        // 调用 performClick，确保无障碍服务兼容
//                        v.performClick();
//                        return true;
//                }
//                return false;
//            });
//        }
//    }

    // 提交按钮逻辑
    private void setupSubmitButton(Button submitButton, List<View> startOptions, List<View> endOptions, List<View> checkbox1Options, List<View> checkbox3Options, Map<View, View> checkboxToOptionMap) {
        submitButton.setOnClickListener(v -> {

            boolean allCorrect = true;

            if (userConnections.size() != correctConnections.size()) {
                allCorrect = false; // 如果连线数量不一致，直接标记为不正确
            }

            for (Pair<CheckBox, CheckBox> connection  : userConnections) {
                CheckBox startCheckbox = connection.first;
                CheckBox endCheckbox = connection.second;

                // 获取 CheckBox 对应的 TextView 值
                String startText = ((TextView) checkboxToOptionMap.get(startCheckbox)).getText().toString();
                String endText = ((TextView) checkboxToOptionMap.get(endCheckbox)).getText().toString();

                if (correctConnections.contains(new Pair<>(startText, endText))) {
                    continue;
                } else {
                    allCorrect = false; // 标记存在错误
                }

//                // 找到对应的 CheckBox
//                CheckBox startCheckbox = (CheckBox) findKeyByValue(checkboxToOptionMap, startTextView);
//                CheckBox endCheckbox = (CheckBox) findKeyByValue(checkboxToOptionMap, endTextView);

                startCheckbox.setChecked(true);
                CompoundButtonCompat.setButtonTintList(startCheckbox, ColorStateList.valueOf(Color.parseColor("#FFCCCB")));
                endCheckbox.setChecked(true);
                CompoundButtonCompat.setButtonTintList(endCheckbox, ColorStateList.valueOf(Color.parseColor("#FFCCCB")));

                createConnection(startCheckbox, endCheckbox, Color.parseColor("#FFCCCB"), false);
            }

            // 遍历正确答案
            for (Pair<String, String> correct : correctConnections) {
                // 根据正确答案的文本找到对应的 TextView
                View startTextView = findViewByText(startOptions, correct.first);
                View endTextView = findViewByText(endOptions, correct.second);

                // 找到对应的 CheckBox
                CheckBox startCheckbox = (CheckBox) findKeyByValue(checkboxToOptionMap, startTextView);
                CheckBox endCheckbox = (CheckBox) findKeyByValue(checkboxToOptionMap, endTextView);

                // 勾选 CheckBox 并设置为绿色
                startCheckbox.setChecked(true);
                CompoundButtonCompat.setButtonTintList(startCheckbox, ColorStateList.valueOf(Color.GREEN));
                endCheckbox.setChecked(true);
                CompoundButtonCompat.setButtonTintList(endCheckbox, ColorStateList.valueOf(Color.GREEN));

                // 绘制绿色连线
                createConnection(startCheckbox, endCheckbox, Color.GREEN);
            }

            disableCheckBoxes(checkbox1Options);
            disableCheckBoxes(checkbox3Options);

            // 提示用户结果
            if (allCorrect) {
                Toast.makeText(getContext(), "恭喜你全部回答正确！", Toast.LENGTH_LONG).show();
            }

        });
    }

    private View findKeyByValue(Map<View, View> map, View value) {
        for (Map.Entry<View, View> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey(); // 返回对应的 Key
            }
        }
        return null; // 未找到匹配的键
    }

    // 检查是否已经存在连线
//    private boolean isConnectionExists(View start, View end) {
//        for (Pair<View, View> connection : userConnections) {
//            if (connection.first == start && connection.second == end) {
//                return true;
//            }
//        }
//        return false;
//    }

    private float[] getCheckBoxCenterCoordinates(CheckBox checkBox) {
        // 获取 CheckBox 在窗口中的位置
        int[] locationInWindow = new int[2];
        checkBox.getLocationInWindow(locationInWindow);

        // 获取 LineView 在窗口中的位置
        int[] lineViewLocationInWindow = new int[2];
        lineView.getLocationInWindow(lineViewLocationInWindow);

        // 计算 CheckBox 相对于 LineView 的位置
        float relativeX = locationInWindow[0] - lineViewLocationInWindow[0] + checkBox.getWidth() / 2f;
        float relativeY = locationInWindow[1] - lineViewLocationInWindow[1] + checkBox.getHeight() / 2f;

        // 如果需要调试，可以输出坐标信息
        // Log.d("Coordinates", "CheckBox at: " + relativeX + ", " + relativeY);

        return new float[]{relativeX, relativeY};
    }

    private void setupClearButton(Button clearButton, List<View> checkbox1Options, List<View> checkbox3Options) {
        clearButton.setOnClickListener(v -> {
            lineView.clearLines();
            userConnections.clear();
            isSelectingStart = true;
            selectedStart = null;
            selectedEnd = null;

            // 清空复选框
            clearCheckBoxes(checkbox1Options);
            clearCheckBoxes(checkbox3Options);

            // 启用所有 CheckBox
            enableCheckBoxes(checkbox1Options);
            enableCheckBoxes(checkbox3Options);
        });
    }

    private void clearCheckBoxes(List<View> options) {
        for (View option : options) {
            if (option instanceof CheckBox) {
                ((CheckBox) option).setChecked(false);
                CompoundButtonCompat.setButtonTintList(((CheckBox) option), ColorStateList.valueOf(Color.BLACK));
            }
        }
    }

    private void disableCheckBoxes(List<View> options) {
        for (View option : options) {
            if (option instanceof CheckBox) {
                option.setEnabled(false); // 禁用 CheckBox
            }
        }
    }

    private void enableCheckBoxes(List<View> options) {
        for (View option : options) {
            if (option instanceof CheckBox) {
                option.setEnabled(true); // 启用 CheckBox
            }
        }
    }
    // 根据触摸点找到目标选项
//    private View findTargetOption(List<View> options, float x, float y) {
//        for (View option : options) {
//            int[] location = new int[2];
//            option.getLocationOnScreen(location); // 获取选项的屏幕位置
//
//            // 检查触摸点是否在选项范围内
//            if (x >= location[0] && x <= location[0] + option.getWidth() &&
//                    y >= location[1] && y <= location[1] + option.getHeight()) {
//                return option;
//            }
//        }
//        return null; // 未找到匹配的终点选项
//    }

    // 根据文字查找 View
    private View findViewByText(List<View> options, String text) {
        for (View option : options) {
            if (option instanceof TextView && ((TextView) option).getText().toString().equals(text)) {
                return option;
            }
        }
        return null;
    }

    private int getCheckedCount(List<View> checkBoxes) {
        int count = 0;
        for (View view : checkBoxes) {
            if (view instanceof CheckBox && ((CheckBox) view).isChecked()) {
                count++;
            }
        }
        return count;
    }
}