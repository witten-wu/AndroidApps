package com.example.myapplication;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class VNESTPageFour2 extends Fragment {

    private static final String ARG_SUBJECT = "subject"; // 参数1
    private static final String ARG_OBJECT = "object";  // 参数2

    private String subject = "工人"; // 用于替换 XX 的主语
    private String object = "板车";  // 用于替换 XX 的宾语

    public VNESTPageFour2() {
        // Required empty public constructor
    }

    // 创建实例时传入参数
    public static VNESTPageFour2 newInstance(String subject, String object) {
        VNESTPageFour2 fragment = new VNESTPageFour2();
        Bundle args = new Bundle();
        args.putString(ARG_SUBJECT, subject);
        args.putString(ARG_OBJECT, object);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            subject = getArguments().getString(ARG_SUBJECT);
            object = getArguments().getString(ARG_OBJECT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vnest_page4_2, container, false);

        // 找到布局中的 TextView
        TextView textView2 = view.findViewById(R.id.textView2);

        SpannableString spannableString2 = new SpannableString(subject + "为什么推" + object + "?");
        spannableString2.setSpan(new UnderlineSpan(), 2, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView2.setText(spannableString2);

        return view;
    }
}