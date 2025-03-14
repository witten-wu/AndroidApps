package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class LanSdyPageOption2_4 extends Fragment {

    private AudioManager audioManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        audioManager = new AudioManager(this, R.raw.g2ainst_final, 8);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.ls_option2_4, container, false);

        ViewPager2 viewPager = requireActivity().findViewById(R.id.viewPager);

        Button previousbutton = view.findViewById(R.id.btnStart);
        previousbutton.setOnClickListener(v -> {
            viewPager.setCurrentItem(9, true);
        });

        return view;
    }
}
