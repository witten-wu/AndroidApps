package com.example.myapplication;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class LanSdyPageOne extends Fragment {

    public interface SelectionCallback {
        void onSelect(String option); // 回调方法传递选择
    }
    private LanSdyPageOne.SelectionCallback callback;

    public LanSdyPageOne(LanSdyPageOne.SelectionCallback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.ls_page1, container, false);

        // 选择 1 按钮
        Button button1 = view.findViewById(R.id.button1);
        button1.setOnClickListener(v -> {
            if (callback != null) {
                callback.onSelect("聽字選圖");
            }
        });

        Button button2 = view.findViewById(R.id.button2);
        button2.setOnClickListener(v -> {
            if (callback != null) {
                callback.onSelect("被動句");
            }
        });

        // 选择 3 按钮
        Button button3 = view.findViewById(R.id.button3);
        button3.setOnClickListener(v -> {
            if (callback != null) {
                callback.onSelect("比較句");
            }
        });

        // 选择 4 按钮
        Button button4 = view.findViewById(R.id.button4);
        button4.setOnClickListener(v -> {
            if (callback != null) {
                callback.onSelect("情態詞");
            }
        });

        return view;
    }
}