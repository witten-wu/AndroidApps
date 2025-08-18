package com.example.myapplication;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class SFAPageFourFragment extends Fragment {
    private static final String ARG_IMAGE_NAME = "image_name";
    private String imageName;
    private ImageView mainImage;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final String TAG = "SFAPageFour";
    private String subjectId;
    private MediaRecorder mediaRecorder; // 录音器
    private boolean isRecording = false; // 是否正在录音
    private String audioFilePath; // 录音文件路径

    public static SFAPageFourFragment newInstance(String imageName) {
        SFAPageFourFragment fragment = new SFAPageFourFragment();
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
        return inflater.inflate(R.layout.sfa_page4, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        subjectId = getSubjectIdFromActivity();

        audioFilePath = generateUniqueAudioPath(subjectId);

        mainImage = view.findViewById(R.id.imageView);

        ImageUtils.loadImageFromAssets(getContext(), mainImage, imageName);

        checkPermissionAndRecord();
    }

    private String getSubjectIdFromActivity() {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            return mainActivity.getActivitySubjectId();
        }
        return "unknown";
    }

    private String generateUniqueAudioPath(String subjectId) {
        String deviceId = getDeviceIdentifier();
        long timestamp = System.currentTimeMillis();

        File deviceFolder = new File(requireContext().getExternalFilesDir(null), deviceId);
        File subjectFolder = new File(deviceFolder, subjectId);
        File experimentFolder = new File(subjectFolder, "SFA");

        // 创建所有必要的目录
        if (!experimentFolder.exists()) {
            boolean created = experimentFolder.mkdirs();
            if (!created) {
                Log.e(TAG, "Failed to create folder structure: " + experimentFolder.getAbsolutePath());
                // 如果创建失败，回退到根目录
                return requireContext().getExternalFilesDir(null).getAbsolutePath() + "/" + timestamp + "_" + imageName + "_step3.3gp";
            }
        }

        // 返回完整的文件路径：deviceId/subjectId/NameScreeners/timestamp.3gp
        return experimentFolder.getAbsolutePath() + "/" + timestamp + "_" + imageName + "_step3.3gp";
    }

    private String getDeviceIdentifier() {
        try {
            String androidId = android.provider.Settings.Secure.getString(
                    requireContext().getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID
            );

            // 确保 Android ID 有效（不为空且不是已知的无效值）
            if (androidId != null && !androidId.isEmpty() && !"9774d56d682e549c".equals(androidId)) {
                return androidId;
            } else {
                // 如果获取不到有效的 Android ID，使用一个默认值
                Log.w(TAG, "Invalid or null Android ID, using default");
                return "unknown";
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to get Android ID", e);
            return "unknown";
        }
    }

    private void checkPermissionAndRecord() {
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
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
            //Toast.makeText(requireContext(), "录音已开始", Toast.LENGTH_SHORT).show();
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
            //Toast.makeText(requireContext(), "录音已停止", Toast.LENGTH_SHORT).show();
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
