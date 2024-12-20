package com.example.myapplication;

import android.content.Intent;
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
    }
}