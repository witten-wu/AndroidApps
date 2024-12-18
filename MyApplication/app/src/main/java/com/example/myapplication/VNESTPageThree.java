package com.example.myapplication;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class VNESTPageThree extends Fragment {

    public interface SelectionCallback {
        void onSelect(String subject, String object); // 回调方法传递选择
    }
    private SelectionCallback callback;

    public VNESTPageThree(SelectionCallback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.vnest_page3, container, false);

        // 选择 C 按钮
        Button buttonC = view.findViewById(R.id.worker_button);
        buttonC.setOnClickListener(v -> {
            if (callback != null) {
                callback.onSelect("工人", "板车"); // 通知 MainActivity
            }
        });

        // 选择 D 按钮
        Button buttonD = view.findViewById(R.id.traveler_button);
        buttonD.setOnClickListener(v -> {
            if (callback != null) {
                callback.onSelect("旅客", "行李"); // 通知 MainActivity
            }
        });

        return view;
    }
}
