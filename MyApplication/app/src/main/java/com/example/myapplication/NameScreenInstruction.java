package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NameScreenInstruction extends Fragment {
    private static final String ARG_INSTRUCTION_TEXT = "instruction_text";
    private String instructionText;

    public static NameScreenInstruction newInstance(String instructionText) {
        NameScreenInstruction fragment = new NameScreenInstruction();
        Bundle args = new Bundle();
        args.putString(ARG_INSTRUCTION_TEXT, instructionText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            instructionText = getArguments().getString(ARG_INSTRUCTION_TEXT, "請說出每個图片的名稱");
        } else {
            instructionText = "請說出每個图片的名稱"; // 默认文本
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.ns_page0, container, false);

        // 设置文本
        TextView questionText = view.findViewById(R.id.questionText);
        questionText.setText(instructionText);

        return view;
    }
}
