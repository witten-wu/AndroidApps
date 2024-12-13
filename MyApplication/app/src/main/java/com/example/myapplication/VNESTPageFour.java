package com.example.myapplication;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class VNESTPageFour extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.vnest_page4, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 找到 TextView
        TextView textView1 = view.findViewById(R.id.textView1);
        TextView textView2 = view.findViewById(R.id.textView2);
        TextView textView3 = view.findViewById(R.id.textView3);

        // 为 TextView1 的 "什么时候" 添加下划线
        SpannableString spannableString1 = new SpannableString("工人什么时候推板车");
        spannableString1.setSpan(new UnderlineSpan(), 2, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView1.setText(spannableString1);

        // 为 TextView2 的 "为什么" 添加下划线
        SpannableString spannableString2 = new SpannableString("工人为什么推板车");
        spannableString2.setSpan(new UnderlineSpan(), 2, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView2.setText(spannableString2);

        // 为 TextView3 的 "在哪里" 添加下划线
        SpannableString spannableString3 = new SpannableString("工人在哪里推板车");
        spannableString3.setSpan(new UnderlineSpan(), 2, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView3.setText(spannableString3);
    }
}
