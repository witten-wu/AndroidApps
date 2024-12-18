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

public class VNESTPageFour extends Fragment {

    private static final String ARG_SUBJECT = "subject"; // 参数1
    private static final String ARG_OBJECT = "object";  // 参数2

    private String subject; // 用于替换 XX 的主语
    private String object;  // 用于替换 XX 的宾语

    public VNESTPageFour() {
        // Required empty public constructor
    }

    // 创建实例时传入参数
    public static VNESTPageFour newInstance(String subject, String object) {
        VNESTPageFour fragment = new VNESTPageFour();
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
        View view = inflater.inflate(R.layout.vnest_page4, container, false);

        // 找到布局中的 TextView
        TextView textView1 = view.findViewById(R.id.textView1);
        TextView textView2 = view.findViewById(R.id.textView2);
        TextView textView3 = view.findViewById(R.id.textView3);

        // 使用传入的参数替换 XX
        SpannableString spannableString1 = new SpannableString(subject + "什么时候推" + object);
        spannableString1.setSpan(new UnderlineSpan(), 2, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView1.setText(spannableString1);

        SpannableString spannableString2 = new SpannableString(subject + "为什么推" + object);
        spannableString2.setSpan(new UnderlineSpan(), 2, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView2.setText(spannableString2);

        SpannableString spannableString3 = new SpannableString(subject + "在哪里推" + object);
        spannableString3.setSpan(new UnderlineSpan(), 2, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView3.setText(spannableString3);

        return view;
    }
}