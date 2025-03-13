package com.example.myapplication;

import android.content.Context;
import android.media.MediaPlayer;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager2.widget.ViewPager2;

/**
 * 音频播放管理器
 * 处理Fragment中的音频播放，自动与Fragment生命周期绑定
 */
public class AudioManager implements DefaultLifecycleObserver {

    private final Fragment fragment;
    private final int audioResourceId;
    private final int fragmentPosition;
    private final int viewPagerId;

    private MediaPlayer mediaPlayer;
    private ViewPager2.OnPageChangeCallback pageChangeCallback;
    private OnAudioCompletionListener completionListener;

    /**
     * 音频完成回调接口
     */
    public interface OnAudioCompletionListener {
        void onCompletion();
    }

    /**
     * 构建音频管理器
     *
     * @param fragment 需要播放音频的Fragment
     * @param audioResourceId 音频资源ID
     * @param fragmentPosition Fragment在ViewPager2中的位置
     * @param viewPagerId ViewPager2的资源ID
     */
    public AudioManager(Fragment fragment, int audioResourceId, int fragmentPosition, int viewPagerId) {
        this.fragment = fragment;
        this.audioResourceId = audioResourceId;
        this.fragmentPosition = fragmentPosition;
        this.viewPagerId = viewPagerId;

        // 添加生命周期观察者
        fragment.getLifecycle().addObserver(this);
    }

    /**
     * 构建音频管理器（使用默认ViewPager2 ID）
     */
    public AudioManager(Fragment fragment, int audioResourceId, int fragmentPosition) {
        this(fragment, audioResourceId, fragmentPosition, R.id.viewPager);
    }

    /**
     * 设置音频完成监听器
     */
    public void setOnAudioCompletionListener(OnAudioCompletionListener listener) {
        this.completionListener = listener;
    }

    /**
     * 初始化音频播放器
     */
    private void initMediaPlayer() {
        Context context = fragment.requireContext();
        try {
            mediaPlayer = MediaPlayer.create(context, audioResourceId);
            if (mediaPlayer != null) {
                mediaPlayer.setOnCompletionListener(mp -> {
                    if (completionListener != null) {
                        completionListener.onCompletion();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取ViewPager2实例
     */
    private ViewPager2 getViewPager() {
        return fragment.requireActivity().findViewById(viewPagerId);
    }

    /**
     * 注册页面变化监听器
     */
    private void setupPageChangeListener() {
        ViewPager2 viewPager = getViewPager();
        if (viewPager != null) {
            pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    if (position == fragmentPosition) {
                        startAudio();
                    } else {
                        pauseAudio();
                    }
                }
            };
            viewPager.registerOnPageChangeCallback(pageChangeCallback);
        }
    }

    /**
     * 开始播放音频
     */
    public void startAudio() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    /**
     * 暂停播放音频
     */
    public void pauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    /**
     * 重新开始播放音频
     */
    public void restartAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        }
    }

    /**
     * 释放资源
     */
    private void releaseResources() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        ViewPager2 viewPager = getViewPager();
        if (viewPager != null && pageChangeCallback != null) {
            viewPager.unregisterOnPageChangeCallback(pageChangeCallback);
            pageChangeCallback = null;
        }
    }

    // 生命周期观察者方法

    @Override
    public void onCreate(LifecycleOwner owner) {
        initMediaPlayer();
    }

    @Override
    public void onStart(LifecycleOwner owner) {
        setupPageChangeListener();
    }

    @Override
    public void onResume(LifecycleOwner owner) {
        ViewPager2 viewPager = getViewPager();
        if (viewPager != null && viewPager.getCurrentItem() == fragmentPosition) {
            startAudio();
        }
    }

    @Override
    public void onPause(LifecycleOwner owner) {
        pauseAudio();
    }

    @Override
    public void onDestroy(LifecycleOwner owner) {
        releaseResources();
        fragment.getLifecycle().removeObserver(this);
    }
}