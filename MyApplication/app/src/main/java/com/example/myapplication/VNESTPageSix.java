package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class VNESTPageSix extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.vnest_page6, container, false);

        // 获取视图中的组件
        LinearLayout boxContainer = view.findViewById(R.id.boxContainer); // 包含三个 Box 的容器
        TextView boxPush = view.findViewById(R.id.boxPush); // "推" 的 Box
        Button showButton = view.findViewById(R.id.showButton); // 显示按钮

        showButton.setOnClickListener(v -> {
            // 检查当前的可见性状态
            if (boxPush.getVisibility() == View.VISIBLE) {
                // 如果当前是可见的，则隐藏
                boxPush.setVisibility(View.INVISIBLE);
            } else {
                // 如果当前是隐藏的，则显示
                boxPush.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }
}