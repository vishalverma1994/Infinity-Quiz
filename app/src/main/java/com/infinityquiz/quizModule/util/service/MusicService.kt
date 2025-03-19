package com.infinityquiz.quizModule.util.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.infinityquiz.R

class MusicService : Service() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.background_music)
        mediaPlayer?.isLooping = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification()
        startForeground(1, notification)
        mediaPlayer?.start()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseMediaPlayer()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    private fun createNotification(): Notification {
        val channelId = "music_channel"
        val channelName = "Music Service"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Create a notification
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Music Service")
            .setContentText("Music is playing")
            .setSmallIcon(R.drawable.ic_volume_up)
            .build()
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.apply {
            stop()    // Stop playback
            release() // Release resources
        }
        mediaPlayer = null // Set to null to prevent memory leaks
    }
}

