package com.example.myapplication;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.view.View;
import android.view.ViewGroup;

public class SFAPageOneFragment extends Fragment {
    private static final String ARG_IMAGE_NAME = "image_name";
    private String imageName;

    public static SFAPageOneFragment newInstance(String imageName) {
        SFAPageOneFragment fragment = new SFAPageOneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_NAME, imageName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageName = getArguments().getString(ARG_IMAGE_NAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.sfa_page1, container, false);

        ImageView imageView = view.findViewById(R.id.imageView);

        // 从assets目录加载图片
        ImageUtils.loadImageFromAssets(getContext(), imageView, imageName);

        return view;
    }
}
