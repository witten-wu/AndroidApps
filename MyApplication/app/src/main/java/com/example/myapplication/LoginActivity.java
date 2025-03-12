package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    private static final String VALID_USERNAME = "demo";
    private static final String VALID_PASSWORD = "demo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 检查是否已登录
        if (isLoggedIn()) {
            startMainActivity();
            return;
        }

        // 初始化控件
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // 验证用户名和密码
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 检查凭据
        if (VALID_USERNAME.equals(username) && VALID_PASSWORD.equals(password)) {
            // 登录成功
            saveLoginStatus(true);
            startMainActivity();
        } else {
            // 登录失败
            Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveLoginStatus(boolean isLoggedIn) {
        SharedPreferences preferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("is_logged_in", isLoggedIn);
        editor.apply();
    }

    private boolean isLoggedIn() {
        SharedPreferences preferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        return preferences.getBoolean("is_logged_in", false);
    }

    private void startMainActivity() {
        // 根据您的逻辑选择一个默认的选项
        Intent intent = new Intent(LoginActivity.this, SelectionActivity.class);
        startActivity(intent);
        finish(); // 结束当前Activity
    }
}