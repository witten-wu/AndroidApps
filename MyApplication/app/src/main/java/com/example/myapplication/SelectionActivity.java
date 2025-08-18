package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SelectionActivity extends AppCompatActivity {

    private String currentSubjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        // 获取 Subject ID
        currentSubjectId = getIntent().getStringExtra("subject_id");
        if (currentSubjectId == null || currentSubjectId.isEmpty()) {
            currentSubjectId = getCurrentSubjectId();
        }

        // 如果还是没有 Subject ID，返回输入界面
        if (currentSubjectId.isEmpty()) {
            startSubjectIdActivity();
            return;
        }

        // 显示当前的 Subject ID（可选）
        TextView subjectIdDisplay = findViewById(R.id.subjectIdDisplay); // 需要在布局中添加
        if (subjectIdDisplay != null) {
            subjectIdDisplay.setText("当前被试: " + currentSubjectId);
        }

        // 选择 A 按钮
        Button buttonA = findViewById(R.id.buttonA);
        buttonA.setOnClickListener(v -> {
            Intent intent = new Intent(SelectionActivity.this, MainActivity.class);
            intent.putExtra("selection", "A"); // 传递选择结果
            intent.putExtra("subject_id", currentSubjectId); // 传递 Subject ID
            startActivity(intent);
        });

        // 选择 B 按钮
        Button buttonB = findViewById(R.id.buttonB);
        buttonB.setOnClickListener(v -> {
            Intent intent = new Intent(SelectionActivity.this, MainActivity.class);
            intent.putExtra("selection", "B"); // 传递选择结果
            intent.putExtra("subject_id", currentSubjectId); // 传递 Subject ID
            startActivity(intent);
        });

        // 选择 C 按钮
        Button buttonC = findViewById(R.id.buttonC);
        buttonC.setOnClickListener(v -> {
            Intent intent = new Intent(SelectionActivity.this, MainActivity.class);
            intent.putExtra("selection", "C"); // 传递选择结果
            intent.putExtra("subject_id", currentSubjectId); // 传递 Subject ID
            startActivity(intent);
        });

        Button buttonD = findViewById(R.id.buttonD);
        buttonD.setOnClickListener(v -> {
            Intent intent = new Intent(SelectionActivity.this, MainActivity.class);
            intent.putExtra("selection", "D"); // 传递选择结果
            intent.putExtra("subject_id", currentSubjectId); // 传递 Subject ID
            startActivity(intent);
        });

        Button buttonE = findViewById(R.id.buttonE);
        buttonE.setOnClickListener(v -> {
            Intent intent = new Intent(SelectionActivity.this, MainActivity.class);
            intent.putExtra("selection", "E"); // 传递选择结果
            intent.putExtra("subject_id", currentSubjectId); // 传递 Subject ID
            startActivity(intent);
        });

        Button buttonF = findViewById(R.id.buttonF);
        buttonF.setOnClickListener(v -> {
            Intent intent = new Intent(SelectionActivity.this, MainActivity.class);
            intent.putExtra("selection", "F"); // 传递选择结果
            intent.putExtra("subject_id", currentSubjectId); // 传递 Subject ID
            startActivity(intent);
        });

        Button buttonG = findViewById(R.id.buttonG);
        buttonG.setOnClickListener(v -> {
            Intent intent = new Intent(SelectionActivity.this, MainActivity.class);
            intent.putExtra("selection", "G"); // 传递选择结果
            intent.putExtra("subject_id", currentSubjectId); // 传递 Subject ID
            startActivity(intent);
        });

        Button buttonH = findViewById(R.id.buttonH);
        buttonH.setOnClickListener(v -> {
            Intent intent = new Intent(SelectionActivity.this, MainActivity.class);
            intent.putExtra("selection", "H"); // 传递选择结果
            intent.putExtra("subject_id", currentSubjectId); // 传递 Subject ID
            startActivity(intent);
        });

        Button buttonI = findViewById(R.id.buttonI);
        buttonI.setOnClickListener(v -> {
            Intent intent = new Intent(SelectionActivity.this, MainActivity.class);
            intent.putExtra("selection", "I"); // 传递选择结果
            intent.putExtra("subject_id", currentSubjectId); // 传递 Subject ID
            startActivity(intent);
        });

        Button buttonJ = findViewById(R.id.buttonJ);
        buttonJ.setOnClickListener(v -> {
            Intent intent = new Intent(SelectionActivity.this, MainActivity.class);
            intent.putExtra("selection", "J"); // 传递选择结果
            intent.putExtra("subject_id", currentSubjectId); // 传递 Subject ID
            startActivity(intent);
        });

//        Button buttonLogout = findViewById(R.id.logoutButton);
//        buttonLogout.setOnClickListener(v -> {
//            // 清除登录状态
//            SharedPreferences preferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putBoolean("is_logged_in", false);
//            editor.apply();
//
//            // 返回登录界面
//            Intent intent = new Intent(SelectionActivity.this, LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//        });

        LinearLayout exchangeSubjectButton = findViewById(R.id.exchangeSubjectButton); // 修改为 LinearLayout
        exchangeSubjectButton.setOnClickListener(v -> {
            // 清除当前的 Subject ID
            clearSubjectId();

            // 返回 Subject ID 输入界面
            Intent intent = new Intent(SelectionActivity.this, SubjectIdActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

//        Button exchangeSubjectButton = findViewById(R.id.exchangeSubjectButton); // 原来的 logoutButton
//        exchangeSubjectButton.setOnClickListener(v -> {
//            // 清除当前的 Subject ID
//            clearSubjectId();
//
//            // 返回 Subject ID 输入界面
//            Intent intent = new Intent(SelectionActivity.this, SubjectIdActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//        });
    }
    private String getCurrentSubjectId() {
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        return preferences.getString("current_subject_id", "");
    }

    private void clearSubjectId() {
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("current_subject_id");
        editor.apply();
    }

    private void startSubjectIdActivity() {
        Intent intent = new Intent(SelectionActivity.this, SubjectIdActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}