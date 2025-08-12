package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SubjectIdActivity extends AppCompatActivity {

    private EditText subjectIdEditText;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_id); // 需要重命名布局文件

        // 检查是否已有 Subject ID
        String currentSubjectId = getCurrentSubjectId();
        if (!currentSubjectId.isEmpty()) {
            startSelectionActivity(currentSubjectId);
            return;
        }

        // 初始化控件
        subjectIdEditText = findViewById(R.id.subjectIdEditText); // 原来的 usernameEditText
        confirmButton = findViewById(R.id.confirmButton); // 原来的 loginButton

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSetSubjectId();
            }
        });
    }

    private void attemptSetSubjectId() {
        String subjectId = subjectIdEditText.getText().toString().trim();

        // 验证 Subject ID
        if (subjectId.isEmpty()) {
            Toast.makeText(this, "Subject ID 不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 可以添加更多验证规则，比如长度、格式等
        if (subjectId.length() < 2) {
            Toast.makeText(this, "Subject ID 至少需要2个字符", Toast.LENGTH_SHORT).show();
            return;
        }

        // 保存 Subject ID
        saveSubjectId(subjectId);
        startSelectionActivity(subjectId);
    }

    private void saveSubjectId(String subjectId) {
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("current_subject_id", subjectId);
        editor.apply();
    }

    private String getCurrentSubjectId() {
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        return preferences.getString("current_subject_id", "");
    }

    private void startSelectionActivity(String subjectId) {
        Intent intent = new Intent(SubjectIdActivity.this, SelectionActivity.class);
        intent.putExtra("subject_id", subjectId);
        startActivity(intent);
        finish(); // 结束当前Activity
    }
}