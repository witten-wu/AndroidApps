package com.example.filesync

import com.jcraft.jsch.*
import java.io.File
import java.io.FileInputStream

class SftpUploader(
    private val host: String,
    private val port: Int = 22,
    private val username: String,
    private val password: String
) {
    fun uploadDirectory(localDir: File, remoteDir: String) {
        val jsch = JSch()
        val session = jsch.getSession(username, host, port)
        session.setPassword(password)
        session.setConfig("StrictHostKeyChecking", "no")
        session.connect()

        val channel = session.openChannel("sftp") as ChannelSftp
        channel.connect()

        uploadRecursively(channel, localDir, remoteDir)

        channel.disconnect()
        session.disconnect()
    }

    private fun uploadRecursively(channel: ChannelSftp, localFile: File, remotePath: String) {
        if (localFile.isDirectory) {
            val newRemotePath = "$remotePath/${localFile.name}"
            ensureRemoteDir(channel, newRemotePath)
            localFile.listFiles()?.forEach {
                uploadRecursively(channel, it, newRemotePath)
            }
        } else {
            val remoteFilePath = "$remotePath/${localFile.name}"
            channel.put(FileInputStream(localFile), remoteFilePath)
        }
    }

    private fun ensureRemoteDir(channel: ChannelSftp, path: String) {
        val folders = path.split("/").filter { it.isNotEmpty() }
        var currentPath = ""
        for (folder in folders) {
            currentPath += "/$folder"
            try {
                channel.cd(currentPath)
            } catch (e: SftpException) {
                channel.mkdir(currentPath)
            }
        }
    }
}