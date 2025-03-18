package com.infinityquiz

import com.infinityquiz.quizModule.util.NavigationModeUtil
import kotlinx.serialization.Serializable

/**
 * application screens
 */
@Serializable
data object HomeRoute

@Serializable
data class QuizRoute(
    val mode: String = NavigationModeUtil.START.name,
)

@Serializable
data class ScoreRoute(
    val correctScore: Int = 0,
    val totalScore: Int = 0
)