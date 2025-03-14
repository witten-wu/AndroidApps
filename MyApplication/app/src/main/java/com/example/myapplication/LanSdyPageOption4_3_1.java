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

public class LanSdyPageOption4_3_1 extends Fragment {

    private AudioManager audioManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        audioManager = new AudioManager(this, R.raw.g2cinst_04, 5);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.ls_option4_3_1, container, false);


        ViewPager2 viewPager = requireActivity().findViewById(R.id.viewPager);
        Button nextbutton = view.findViewById(R.id.btnNext);
        nextbutton.setOnClickListener(v -> {
            viewPager.setCurrentItem(6, true);
        });

        return view;
    }

}
