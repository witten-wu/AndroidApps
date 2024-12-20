package com.example.myapplication;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class LanSdyPageOption1_1 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.ls_option1_1, container, false);

        ViewPager2 viewPager = requireActivity().findViewById(R.id.viewPager);
        Button nextButton = view.findViewById(R.id.btnNext);
        nextButton.setOnClickListener(v -> {
            viewPager.setCurrentItem(3, true);
        });

        return view;
    }
}
