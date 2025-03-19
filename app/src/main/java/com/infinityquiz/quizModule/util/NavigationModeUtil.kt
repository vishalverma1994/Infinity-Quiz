package com.infinityquiz.quizModule.util

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.infinityquiz.quizModule.util.service.MusicService

enum class NavigationModeUtil {
    START,
    BOOKMARK
}

//method is used to share the text on apps
fun shareText(context: Context, text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Share Question"))
}

/**
 * Starts the music service as a foreground service.
 *
 * This function initiates the `MusicService` using a foreground service intent.
 * Foreground services are important for tasks that the user is aware of, such as music playback,
 * as they have a higher priority and are less likely to be killed by the system when resources are low.
 */
fun startMusic(context: Context) {
    val intent = Intent(context, MusicService::class.java)
    ContextCompat.startForegroundService(context, intent)
}

/**
 * Stops the music playback service.
 *
 * This function sends a stop command to the `MusicService`, effectively halting
 * any ongoing music playback managed by that service. It relies on the `MusicService`
 * to handle the actual termination of the music player.
 */
fun stopMusic(context: Context) {
    val intent = Intent(context, MusicService::class.java)
    context.stopService(intent)
}