package com.example.filesync

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.File
import java.util.concurrent.TimeUnit

class AutoUploadWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        return try {
            val uploader = SftpUploader(
                host = "192.168.1.107",
                username = "wyd",
                password = "3349"
            )

            val rootDir = File("/storage/emulated/0/ExperimentData")
            if (rootDir.exists() && rootDir.isDirectory) {
                val subjectDirs = rootDir.listFiles { file -> file.isDirectory }
                subjectDirs?.forEach { subjectDir ->
                    uploader.uploadDirectory(subjectDir, "/home/wyd/Desktop/")
                }
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry() // 或 Result.failure()，根据实际需求决定
        }
    }
}