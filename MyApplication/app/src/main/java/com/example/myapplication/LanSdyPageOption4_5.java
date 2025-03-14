package com.example.myapplication;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class LanSdyPageOption4_5 extends Fragment {

    private static final String ARG_IMAGES = "images"; // 图片资源数组
    private static final String ARG_INDEX = "index";   // 当前图片索引
    private static final String ARG_ITEMNAMES = "itemname";   // 当前图片索引
    private static final String ARG_CORRECT_ANSWER = "correctAnswer"; // 正确答案索引
    private static final String ARG_TITLE_AUDIO = "titleAudio"; // 标题音频资源ID

    private static final int AUDIO_CORRECT = R.raw.correct; // 正确反馈音频
    private static final int AUDIO_WRONG = R.raw.wrong; // 错误反馈音频

    private int[] images; // 图片资源数组
    private int currentIndex; // 当前索引
    private String[] itemnames; // 图片资源数组
    private int[] correctAnswers; // 正确答案索引数组
    private int[] titleAudios; // 标题音频资源ID数组

    private MediaPlayer mediaPlayer;
    private ViewPager2 viewPager;
    private ImageView image1, image2, btnPlay, btnClose;
    private TextView tvProgress, tvTitle;
    private ProgressBar progressBar;

    // 实例化 Fragment，并传入图片资源和索引
    public static LanSdyPageOption4_5 newInstance(int[] images, int index, String[] itemname, int[] correctAnswer, int[] titleAudioResId) {
        LanSdyPageOption4_5 fragment = new LanSdyPageOption4_5();
        Bundle args = new Bundle();
        args.putIntArray(ARG_IMAGES, images);
        args.putInt(ARG_INDEX, index);
        args.putStringArray(ARG_ITEMNAMES, itemname);
        args.putIntArray(ARG_CORRECT_ANSWER, correctAnswer);
        args.putIntArray(ARG_TITLE_AUDIO, titleAudioResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.ls_option4_5, container, false);

        viewPager = requireActivity().findViewById(R.id.viewPager);

        initViews(view);
        loadPageContent();
        setupListeners();

        return view;
    }

    private void initViews(View view) {
        image1 = view.findViewById(R.id.image1);
        image2 = view.findViewById(R.id.image2);
        btnPlay = view.findViewById(R.id.btnPlay);
        btnClose = view.findViewById(R.id.btnClose);
        tvProgress = view.findViewById(R.id.tvProgress);
        tvTitle = view.findViewById(R.id.tvTitle);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void loadPageContent() {
        Bundle args = getArguments();
        if (args != null) {
            images = args.getIntArray(ARG_IMAGES);
            currentIndex = args.getInt(ARG_INDEX);
            itemnames = args.getStringArray(ARG_ITEMNAMES);
            correctAnswers = args.getIntArray(ARG_CORRECT_ANSWER);
            titleAudios = args.getIntArray(ARG_TITLE_AUDIO);

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

                // 设置标题和进度
                if (itemnames != null && currentIndex / 2 < itemnames.length) {
                    tvTitle.setText(itemnames[currentIndex / 2]);
                }

                int totalGroups = (int) Math.ceil(images.length / 2.0);
                int currentGroup = (currentIndex / 2) + 1;
                progressBar.setMax(totalGroups);
                progressBar.setProgress(currentGroup);
                tvProgress.setText(currentGroup + " / " + totalGroups);
            }
        }
    }

    private void setupListeners() {
        // 标题音频播放按钮
        btnPlay.setOnClickListener(v -> {
            int currentGroup = currentIndex / 2;
            if (titleAudios != null && currentGroup < titleAudios.length) {
                playAudio(titleAudios[currentGroup]);
            }
        });

        // 关闭按钮
        btnClose.setOnClickListener(v -> {
            requireActivity().finish(); // 或其他返回逻辑
        });

        // 图片1点击事件
        image1.setOnClickListener(v -> {
            int currentGroup = currentIndex / 2;
            boolean isCorrect = false;

            if (correctAnswers != null && currentGroup < correctAnswers.length) {
                isCorrect = (correctAnswers[currentGroup] == 0); // 0表示第一张图是正确答案
            }

            handleImageSelection(image1, isCorrect ? AUDIO_CORRECT : AUDIO_WRONG, isCorrect);
        });

        // 图片2点击事件
        image2.setOnClickListener(v -> {
            if (image2.getVisibility() == View.VISIBLE) {
                int currentGroup = currentIndex / 2;
                boolean isCorrect = false;

                if (correctAnswers != null && currentGroup < correctAnswers.length) {
                    isCorrect = (correctAnswers[currentGroup] == 1); // 1表示第二张图是正确答案
                }

                handleImageSelection(image2, isCorrect ? AUDIO_CORRECT : AUDIO_WRONG, isCorrect);
            }
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

        // 如果是正确答案，等待音频播放完毕后加载下一页或完成
        if (isCorrect) {
            mediaPlayer.setOnCompletionListener(mp -> {
                // 延迟一小段时间后继续，让用户看清反馈
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (images != null && currentIndex + 2 < images.length) {
                        // 还有更多图片，加载下一组
                        loadNextPage();
                    } else {
                        // 已完成所有图片，进行下一步操作
                        if (viewPager != null) {
                            viewPager.setCurrentItem(1, false);
                        }
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
        // 重置所有图片的透明度
        image1.setAlpha(1.0f);
        image2.setAlpha(1.0f);
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

    // 加载下一组图片
    private void loadNextPage() {
        if (images != null && currentIndex + 2 < images.length) {
            // 替换当前 Fragment，加载下一组图片
            // 注意：需要将正确答案索引和标题音频ID一起传递给下一页
            ((MainActivity) requireActivity()).updateLanSdyPageOption4_5(
                    images,
                    currentIndex + 2,
                    itemnames,
                    correctAnswers, // 保持正确答案索引一致，或者根据需要修改
                    titleAudios // 传递标题音频ID
            );
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // 页面显示时自动播放标题音频
        int currentGroup = currentIndex / 2;
        if (titleAudios != null && currentGroup < titleAudios.length) {
            playAudio(titleAudios[currentGroup]);
        }
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
