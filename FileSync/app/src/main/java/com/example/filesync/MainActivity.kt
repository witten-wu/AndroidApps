package com.example.filesync

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var subject001Btn: Button
    private lateinit var progressBar001: ProgressBar

//    private val requestPermissionLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { granted ->
//        if (granted) {
//            uploadFiles()
//        } else {
//            Toast.makeText(this, "权限被拒绝，无法上传数据", Toast.LENGTH_SHORT).show()
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        subject001Btn = findViewById(R.id.subject001Btn)
        progressBar001 = findViewById(R.id.progressBar001)

        subject001Btn.setOnClickListener {
            checkAndRequestPermission("001")
        }
    }

    private fun checkAndRequestPermission(subjectId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+ 需要检查 MANAGE_EXTERNAL_STORAGE
            if (Environment.isExternalStorageManager()) {
                uploadFiles(subjectId)
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                        data = Uri.fromParts("package", packageName, null)
                    }
                    startActivity(intent)
                }
            }
        } else {
            // Android 10 及以下版本
//            val permission = Manifest.permission.READ_EXTERNAL_STORAGE
//            when {
//                ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
//                    uploadFiles(subjectId)
//                }
//                ActivityCompat.shouldShowRequestPermissionRationale(this, permission) -> {
//                    showPermissionRationaleDialog(permission)
//                }
//                else -> {
//                    requestPermissionLauncher.launch(permission)
//                }
//            }
        }
    }

//    private fun showPermissionRationaleDialog(permission: String) {
//        AlertDialog.Builder(this)
//            .setTitle("权限请求")
//            .setMessage("我们需要访问存储权限来上传文件，请授予权限。")
//            .setPositiveButton("确定") { _, _ ->
//                requestPermissionLauncher.launch(permission)
//            }
//            .setNegativeButton("取消", null)
//            .show()
//    }

    private fun uploadFiles(subjectId: String) {
        // 显示进度条，禁用按钮
        progressBar001.visibility = View.VISIBLE
        subject001Btn.isEnabled = false

        Thread {
            try {
                val uploader = SftpUploader(
                    host = "192.168.1.107",
                    username = "wyd",
                    password = "3349"
                )
                val experimentDir = File("/storage/emulated/0/ExperimentData/$subjectId")
                uploader.uploadDirectory(experimentDir, "/home/wyd/Desktop/")

                runOnUiThread {
                    // 上传成功：隐藏进度条，按钮变绿
                    progressBar001.visibility = View.GONE
                    subject001Btn.isEnabled = true
                    subject001Btn.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(this, R.color.teal_700)
                    )
                    Toast.makeText(this, "Subject $subjectId 上传完成！", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    progressBar001.visibility = View.GONE
                    subject001Btn.isEnabled = true
                    Toast.makeText(this, "上传失败: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }
}