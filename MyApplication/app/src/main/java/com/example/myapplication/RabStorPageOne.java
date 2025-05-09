package com.example.myapplication;

import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;
import java.io.IOException;

public class RabStorPageOne extends Fragment {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    // 图片资源数组
    private final int[] images = {
            R.drawable.rbs_1, R.drawable.rbs_2, R.drawable.rbs_3,
            R.drawable.rbs_4, R.drawable.rbs_5, R.drawable.rbs_6,
            R.drawable.rbs_7, R.drawable.rbs_8, R.drawable.rbs_9,
            R.drawable.rbs_10, R.drawable.rbs_11, R.drawable.rbs_12,
            R.drawable.rbs_13, R.drawable.rbs_14, R.drawable.rbs_15
    };

    private MediaRecorder mediaRecorder; // 录音器
    private boolean isRecording = false; // 是否正在录音
    private String audioFilePath; // 录音文件路径

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.rs_page1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        audioFilePath = requireContext().getExternalFilesDir(null).getAbsolutePath() + "/recording.3gp";

        // 找到 ViewPager2
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);

        // 初始化适配器并设置到 ViewPager2
        ImagePagerAdapter adapter = new ImagePagerAdapter(getContext(), images);
        viewPager.setAdapter(adapter);

        checkPermissionAndRecord();

        viewPager.getChildAt(0).setOnTouchListener((v, event) -> {
            // 告诉父视图不要拦截触摸事件
            v.getParent().requestDisallowInterceptTouchEvent(true);

            // 检测点击事件
            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.performClick(); // 调用 performClick() 处理点击
            }

            return false; // 返回 false 以允许其他事件（如滑动）继续处理
        });
    }

    private void checkPermissionAndRecord() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // 动态申请录音权限
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
            return;
        }

        // 有权限，开始录音
        startRecording();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户授予权限，开始录音
                startRecording();
            } else {
                // 用户拒绝权限
                Toast.makeText(requireContext(), "录音功能需要麦克风权限", Toast.LENGTH_SHORT).show();
                showPermissionExplanation();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // 当Fragment暂停时停止录音
        stopRecording();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 确保录音停止并释放资源
        stopRecording();
    }

    private void showPermissionExplanation() {
        // 可以显示对话框解释为何需要权限
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("需要录音权限")
                .setMessage("此功能需要录音权限才能正常工作。请到设置中开启麦克风权限。")
                .setPositiveButton("去设置", (dialog, which) -> {
                    // 跳转到应用设置界面
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // 开始录音的方法
    private void startRecording() {
        if (isRecording) return; // 如果已经在录音，直接返回

        try {
            // 初始化 MediaRecorder
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 使用麦克风
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); // 文件格式
            mediaRecorder.setOutputFile(audioFilePath); // 保存路径
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); // 音频编码

            mediaRecorder.prepare(); // 准备录音
            mediaRecorder.start(); // 开始录音
            isRecording = true;
            Toast.makeText(requireContext(), "录音已开始", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "录音失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "录音出错: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // 停止录音的方法
    private void stopRecording() {
        if (!isRecording || mediaRecorder == null) return; // 如果没有在录音，直接返回

        try {
            mediaRecorder.stop(); // 停止录音
            Toast.makeText(requireContext(), "录音已停止", Toast.LENGTH_SHORT).show();
        } catch (RuntimeException e) {
            // 录音时间太短可能会导致异常
            e.printStackTrace();
            Toast.makeText(requireContext(), "录音时间太短或出现错误", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                mediaRecorder.release(); // 释放资源
            } catch (Exception e) {
                e.printStackTrace();
            }
            mediaRecorder = null;
            isRecording = false;
        }
    }
}