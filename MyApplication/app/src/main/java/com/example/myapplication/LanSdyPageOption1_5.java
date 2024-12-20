package com.example.myapplication;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class LanSdyPageOption1_5 extends Fragment {

    private static final String ARG_IMAGES = "images"; // 图片资源数组
    private static final String ARG_INDEX = "index";   // 当前图片索引

    private static final String ARG_ITEMNAMES = "itemname";   // 当前图片索引

    private int[] images; // 图片资源数组
    private int currentIndex; // 当前索引

    private String[] itemnames; // 图片资源数组

    // 实例化 Fragment，并传入图片资源和索引
    public static LanSdyPageOption1_5 newInstance(int[] images, int index, String[] itemname) {
        LanSdyPageOption1_5 fragment = new LanSdyPageOption1_5();
        Bundle args = new Bundle();
        args.putIntArray(ARG_IMAGES, images);
        args.putInt(ARG_INDEX, index);
        args.putStringArray(ARG_ITEMNAMES, itemname);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.ls_option1_5, container, false);

        ImageView imageLeft = view.findViewById(R.id.imageLeft);
        ImageView imageRight = view.findViewById(R.id.imageRight);
        TextView tvProgress = view.findViewById(R.id.tvProgress);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);

        Bundle args = getArguments();
        if (args != null) {
            images = args.getIntArray(ARG_IMAGES);
            currentIndex = args.getInt(ARG_INDEX);
            itemnames = args.getStringArray(ARG_ITEMNAMES);

            // 设置图片
            if (images != null && currentIndex < images.length - 1) {
                imageLeft.setImageResource(images[currentIndex]);
                imageRight.setImageResource(images[currentIndex + 1]);

                if (itemnames != null && currentIndex / 2 < itemnames.length) {
                    tvTitle.setText(itemnames[currentIndex / 2]);
                }

                // 设置进度条
                int progress = currentIndex / 2 + 1;
                progressBar.setMax(images.length / 2);
                progressBar.setProgress(progress);
                tvProgress.setText(progress + " / " + (images.length / 2));
            }
        }

        // 左侧图片点击事件
        imageLeft.setOnClickListener(v -> loadNextPage());

        // 右侧图片点击事件
        imageRight.setOnClickListener(v -> loadNextPage());

        return view;
    }

    // 加载下一组图片
    private void loadNextPage() {
        if (images != null && currentIndex + 2 < images.length) {
            // 替换当前 Fragment，加载下一组图片
            ((MainActivity) requireActivity()).updateLanSdyPageOption1_5(images, currentIndex + 2, itemnames);
        }
    }
}
