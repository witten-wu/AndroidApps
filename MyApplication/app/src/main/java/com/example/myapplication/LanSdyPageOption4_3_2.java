package com.example.myapplication;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class LanSdyPageOption4_3_2 extends Fragment {

    private static final int AUDIO_TITLE = R.raw.g2cinst_05; // 标题音频
    private static final int AUDIO_CORRECT = R.raw.g2cinst_a101; // 正确反馈音频
    private static final int AUDIO_WRONG1 = R.raw.g2cinst_a102; // 错误1反馈音频

    private MediaPlayer mediaPlayer;
    private ViewPager2 viewPager;

    private ImageView image1, image2, btnPlay, btnClose;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.ls_option4_3_2, container, false);

        viewPager = requireActivity().findViewById(R.id.viewPager);

        initViews(view);

        setupListeners();

        return view;
    }

    private void initViews(View view) {
        image1 = view.findViewById(R.id.image1);
        image2 = view.findViewById(R.id.image2);
        btnPlay = view.findViewById(R.id.btnPlay);
        btnClose = view.findViewById(R.id.btnClose);
    }
    private void setupListeners() {
        // 标题音频播放按钮
        btnPlay.setOnClickListener(v -> {
            playAudio(AUDIO_TITLE);
        });

        // 关闭按钮
        btnClose.setOnClickListener(v -> {
            requireActivity().finish(); // 或其他返回逻辑
        });

        // 图片1点击事件（假设是错误答案）
        image1.setOnClickListener(v -> {
            handleImageSelection(image1, AUDIO_CORRECT, true);
        });

        // 图片2点击事件（假设是正确答案）
        image2.setOnClickListener(v -> {
            handleImageSelection(image2, AUDIO_WRONG1, false);
        });
    }

    private void handleImageSelection(ImageView selectedImage, int audioResId, boolean isCorrect) {
        // 停止之前的音频播放
        stopCurrentAudio();

        // 禁用所有图片点击，避免多次点击
        setImagesClickable(false);

        // 高亮显示选中的图片
        highlightSelectedImage(selectedImage);

        // 播放反馈音频
        playAudio(audioResId);

        // 如果是正确答案，等待音频播放完毕后跳转到下一页
        if (isCorrect) {
            mediaPlayer.setOnCompletionListener(mp -> {
                // 延迟一小段时间后跳转，让用户看清反馈
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (viewPager != null) {
                        viewPager.setCurrentItem(7, true);
                    }
                }, 500); // 500毫秒延迟
            });
        } else {
            // 如果是错误答案，音频播放完成后恢复界面状态
            mediaPlayer.setOnCompletionListener(mp -> {
                resetImageSelections();
                setImagesClickable(true);
            });
        }
    }

    private void highlightSelectedImage(ImageView selectedImage) {
        // 将其他图片变暗
        image1.setAlpha(selectedImage == image1 ? 1.0f : 0.5f);
        image2.setAlpha(selectedImage == image2 ? 1.0f : 0.5f);
    }

    private void resetImageSelections() {
        // 重置所有图片的透明度和背景
        image1.setAlpha(1.0f);
        image2.setAlpha(1.0f);

        image1.setBackgroundResource(R.color.white);
        image2.setBackgroundResource(R.color.white);
    }

    private void setImagesClickable(boolean clickable) {
        btnPlay.setClickable(clickable);
        image1.setClickable(clickable);
        image2.setClickable(clickable);
    }

    private void playAudio(int audioResId) {
        try {
            stopCurrentAudio();
            mediaPlayer = MediaPlayer.create(requireContext(), audioResId);
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopCurrentAudio() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // 页面显示时自动播放标题音频
        playAudio(AUDIO_TITLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopCurrentAudio();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopCurrentAudio();
    }

}
