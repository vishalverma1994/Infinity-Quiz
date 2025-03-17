package com.infinityquiz.quizModule.data.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.infinityquiz.quizModule.data.local.QuizEntity

@Keep
data class QuizDto(
    @SerializedName("quizList") val quizList: List<QuizEntity>? = emptyList()
)