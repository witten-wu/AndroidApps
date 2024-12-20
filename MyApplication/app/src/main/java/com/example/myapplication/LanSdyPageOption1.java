package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class LanSdyPageOption1 extends Fragment {

    private static final String ARG_OPTION = "option";

    private String option = "option";

    public LanSdyPageOption1() {
        // Required empty public constructor
    }

    // 创建实例时传入参数
    public static LanSdyPageOption1 newInstance(String option) {
        LanSdyPageOption1 fragment = new LanSdyPageOption1();
        Bundle args = new Bundle();
        args.putString(ARG_OPTION, option);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            option = getArguments().getString(ARG_OPTION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ls_option1, container, false);

        // 找到布局中的 TextView
        TextView textView1 = view.findViewById(R.id.tvTitle);

        textView1.setText(option);

        ViewPager2 viewPager = requireActivity().findViewById(R.id.viewPager);
        Button startTrainingButton = view.findViewById(R.id.btnStartTraining);
        startTrainingButton.setOnClickListener(v -> {
            viewPager.setCurrentItem(2, true);
        });

        return view;
    }
}