package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VNESTPageFive extends Fragment {

    private LinearLayout questionsContainer; // 问题容器
    private final List<VNESTPageFive.Question> questions = new ArrayList<>(); // 问题列表
    private static int currentId = 1; // 全局唯一 ID

    public VNESTPageFive() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.vnest_page5, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 获取问题容器和提交按钮
        questionsContainer = view.findViewById(R.id.questionsContainer);
        Button submitButton = view.findViewById(R.id.submitButton);

        questions.clear();

        // 初始化问题数据
        initializeQuestions();

        // 打乱问题顺序
        Collections.shuffle(questions);

        // 动态生成问题并添加到页面
        for (VNESTPageFive.Question question : questions) {
            addQuestionToLayout(question);
        }

        // 设置提交按钮点击事件
        submitButton.setOnClickListener(v -> {
            for (int i = 0; i < questionsContainer.getChildCount(); i++) {
                View childView = questionsContainer.getChildAt(i);
                if (childView instanceof LinearLayout) {
                    LinearLayout questionLayout = (LinearLayout) childView;

                    // 获取 RadioGroup
                    RadioGroup radioGroup = (RadioGroup) questionLayout.getChildAt(1); // 第二个子视图是 RadioGroup

                    // 清除之前的高亮状态
                    clearHighlight(radioGroup);

                    // 获取正确答案的 ID
                    int correctAnswerId = (int) questionLayout.getTag();

                    // 获取用户选择的选项
                    int selectedButtonId = radioGroup.getCheckedRadioButtonId();

                    if (selectedButtonId == -1 || selectedButtonId != correctAnswerId) {
                        // 用户未选择选项 或 用户选择错误

                        // 高亮错误选项（如果有选择）
                        if (selectedButtonId != -1) {
                            View incorrectButton = radioGroup.findViewById(selectedButtonId);
                            if (incorrectButton != null) {
                                incorrectButton.setBackgroundColor(Color.parseColor("#FFCDD2")); // 浅红色
                            }
                        }

                        // 高亮正确选项
                        View correctButton = radioGroup.findViewById(correctAnswerId);
                        if (correctButton != null) {
                            correctButton.setBackgroundColor(Color.parseColor("#C8E6C9")); // 浅绿色
                        }
                    }
                }
            }
        });
    }

    // 初始化问题数据
    private void initializeQuestions() {
        questions.add(new VNESTPageFive.Question("板车推工人", false));
        questions.add(new VNESTPageFive.Question("金鱼推板车", false));
        questions.add(new VNESTPageFive.Question("工人推围巾", false));
        questions.add(new VNESTPageFive.Question("工人推板车", true));
    }

    // 动态将问题添加到布局
    // 动态将问题添加到布局
    private void addQuestionToLayout(VNESTPageFive.Question question) {
        // 每个问题布局为一个水平的 LinearLayout
        LinearLayout questionLayout = new LinearLayout(getContext());
        questionLayout.setOrientation(LinearLayout.HORIZONTAL); // 水平排列
        questionLayout.setGravity(Gravity.CENTER_HORIZONTAL); // 整体居中对齐
        questionLayout.setPadding(8, 8, 8, 8);
        // 动态生成正确答案的 ID
        int correctAnswerId = generateUniqueId();

        // 将正确答案的 ID 存储在 Tag 中
        questionLayout.setTag(correctAnswerId);

        // 添加问题文本
        TextView questionText = new TextView(getContext());
        questionText.setText(question.text);
        questionText.setTextSize(16);
        questionText.setTextColor(Color.BLACK);
        questionText.setGravity(Gravity.CENTER_VERTICAL); // 垂直居中
        LinearLayout.LayoutParams questionTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //questionTextParams.setMarginEnd(32); // 设置右边距，避免文本和选项过于靠近
        questionText.setLayoutParams(questionTextParams);
        questionLayout.addView(questionText);

        // 创建 RadioGroup（水平排列选项）
        RadioGroup radioGroup = new RadioGroup(getContext());
        radioGroup.setOrientation(RadioGroup.HORIZONTAL); // 水平排列选项
        LinearLayout.LayoutParams radioGroupParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        radioGroup.setLayoutParams(radioGroupParams);

        // 创建选项 A
        RadioButton buttonA = new RadioButton(getContext());
        buttonA.setText("正确");
        buttonA.setGravity(Gravity.CENTER);
        buttonA.setPadding(8, 8, 8, 8); // 设置选项的内边距
        if (question.correctAnswer) { // 如果 A 是正确答案
            buttonA.setId(correctAnswerId);
        } else {
            buttonA.setId(generateUniqueId());
        }
        radioGroup.addView(buttonA);

        // 创建选项 B
        RadioButton buttonB = new RadioButton(getContext());
        buttonB.setText("错误");
        buttonB.setGravity(Gravity.CENTER);
        buttonB.setPadding(8, 8, 8, 8); // 设置选项的内边距
        if (!question.correctAnswer) { // 如果 B 是正确答案
            buttonB.setId(correctAnswerId);
        } else {
            buttonB.setId(generateUniqueId());
        }
        radioGroup.addView(buttonB);

        // 添加 RadioGroup 到布局
        questionLayout.addView(radioGroup);

        // 添加问题布局到容器
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        containerParams.setMargins(8, 8, 8, 8); // 间距调整
        questionLayout.setLayoutParams(containerParams);

        questionsContainer.addView(questionLayout);
    }

    // 生成全局唯一 ID
    private int generateUniqueId() {
        return currentId++;
    }

    // 清除高亮状态
    private void clearHighlight(RadioGroup radioGroup) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View child = radioGroup.getChildAt(i);
            child.setBackgroundColor(Color.TRANSPARENT); // 重置背景色
        }
    }

    // 问题类
    private static class Question {
        String text;
        boolean correctAnswer;

        Question(String text, boolean correctAnswer) {
            this.text = text;
            this.correctAnswer = correctAnswer;
        }
    }
}
