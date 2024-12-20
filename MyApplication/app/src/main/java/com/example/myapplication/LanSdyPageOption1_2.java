package com.example.myapplication;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class LanSdyPageOption1_2 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.ls_option1_2, container, false);

        ImageView imageWatermelon = view.findViewById(R.id.imageWatermelon);
        ImageView imageSnail = view.findViewById(R.id.imageSnail);

        // 设置置灰效果
        setImageGray(imageWatermelon);
        setImageGray(imageSnail);

        ViewPager2 viewPager = requireActivity().findViewById(R.id.viewPager);
        Button nextbutton = view.findViewById(R.id.btnNext);
        nextbutton.setOnClickListener(v -> {
            viewPager.setCurrentItem(4, true);
        });

        Button previousbutton = view.findViewById(R.id.btnPrevious);
        previousbutton.setOnClickListener(v -> {
            viewPager.setCurrentItem(2, true);
        });

        return view;
    }

    private void setImageGray(ImageView imageView) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0); // 设置饱和度为 0（灰色）
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        imageView.setColorFilter(colorFilter); // 应用颜色过滤器
    }
}
