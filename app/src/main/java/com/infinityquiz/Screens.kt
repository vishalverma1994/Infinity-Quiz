package com.infinityquiz

import kotlinx.serialization.Serializable

/**
 * application screens
 */
@Serializable
data object HomeRoute

@Serializable
data class QuizRoute(
    val time: Long = 0L
)