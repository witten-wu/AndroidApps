package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        // 选择 A 按钮
        Button buttonA = findViewById(R.id.buttonA);
        buttonA.setOnClickListener(v -> {
            Intent intent = new Intent(SelectionActivity.this, MainActivity.class);
            intent.putExtra("selection", "A"); // 传递选择结果
            startActivity(intent);
        });

        // 选择 B 按钮
        Button buttonB = findViewById(R.id.buttonB);
        buttonB.setOnClickListener(v -> {
            Intent intent = new Intent(SelectionActivity.this, MainActivity.class);
            intent.putExtra("selection", "B"); // 传递选择结果
            startActivity(intent);
        });

        // 选择 C 按钮
        Button buttonC = findViewById(R.id.buttonC);
        buttonC.setOnClickListener(v -> {
            Intent intent = new Intent(SelectionActivity.this, MainActivity.class);
            intent.putExtra("selection", "C"); // 传递选择结果
            startActivity(intent);
        });

        Button buttonD = findViewById(R.id.buttonD);
        buttonD.setOnClickListener(v -> {
            Intent intent = new Intent(SelectionActivity.this, MainActivity.class);
            intent.putExtra("selection", "D"); // 传递选择结果
            startActivity(intent);
        });

        Button buttonE = findViewById(R.id.buttonE);
        buttonE.setOnClickListener(v -> {
            Intent intent = new Intent(SelectionActivity.this, MainActivity.class);
            intent.putExtra("selection", "E"); // 传递选择结果
            startActivity(intent);
        });

        Button buttonF = findViewById(R.id.buttonF);
        buttonF.setOnClickListener(v -> {
            Intent intent = new Intent(SelectionActivity.this, MainActivity.class);
            intent.putExtra("selection", "F"); // 传递选择结果
            startActivity(intent);
        });

        Button buttonLogout = findViewById(R.id.logoutButton);
        buttonLogout.setOnClickListener(v -> {
            // 清除登录状态
            SharedPreferences preferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("is_logged_in", false);
            editor.apply();

            // 返回登录界面
            Intent intent = new Intent(SelectionActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}