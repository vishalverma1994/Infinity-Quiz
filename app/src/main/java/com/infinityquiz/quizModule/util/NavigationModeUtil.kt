package com.infinityquiz.quizModule.util

import android.content.Context
import android.content.Intent

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