package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

        ImageView image1 = view.findViewById(R.id.image1);
        ImageView image2 = view.findViewById(R.id.image2);
        ImageView image3 = view.findViewById(R.id.image3);
        ImageView image4 = view.findViewById(R.id.image4);
        TextView tvProgress = view.findViewById(R.id.tvProgress);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);

        Bundle args = getArguments();
        if (args != null) {
            images = args.getIntArray(ARG_IMAGES);
            currentIndex = args.getInt(ARG_INDEX);
            itemnames = args.getStringArray(ARG_ITEMNAMES);

            if (images != null && currentIndex < images.length) {
                // 第一张图片总是可以显示
                image1.setImageResource(images[currentIndex]);

                // 检查并设置第二张图片
                if (currentIndex + 1 < images.length) {
                    image2.setImageResource(images[currentIndex + 1]);
                    image2.setVisibility(View.VISIBLE);
                } else {
                    image2.setVisibility(View.INVISIBLE);
                }

                // 检查并设置第三张图片
                if (currentIndex + 2 < images.length) {
                    image3.setImageResource(images[currentIndex + 2]);
                    image3.setVisibility(View.VISIBLE);
                } else {
                    image3.setVisibility(View.INVISIBLE);
                }

                // 检查并设置第四张图片
                if (currentIndex + 3 < images.length) {
                    image4.setImageResource(images[currentIndex + 3]);
                    image4.setVisibility(View.VISIBLE);
                } else {
                    image4.setVisibility(View.INVISIBLE);
                }

                // 设置标题和进度（与前面相同）
                if (itemnames != null && currentIndex / 4 < itemnames.length) {
                    tvTitle.setText(itemnames[currentIndex / 4]);
                }

                int totalGroups = (int) Math.ceil(images.length / 4.0);
                int currentGroup = (currentIndex / 4) + 1;
                progressBar.setMax(totalGroups);
                progressBar.setProgress(currentGroup);
                tvProgress.setText(currentGroup + " / " + totalGroups);
            }
        }

        image1.setOnClickListener(v -> loadNextPage());
        image2.setOnClickListener(v -> loadNextPage());
        image3.setOnClickListener(v -> loadNextPage());
        image4.setOnClickListener(v -> loadNextPage());

        return view;
    }

    // 加载下一组图片
    private void loadNextPage() {
        if (images != null && currentIndex + 4 < images.length) {
            // 替换当前 Fragment，加载下一组图片
            ((MainActivity) requireActivity()).updateLanSdyPageOption1_5(images, currentIndex + 4, itemnames);
        }
    }
}
