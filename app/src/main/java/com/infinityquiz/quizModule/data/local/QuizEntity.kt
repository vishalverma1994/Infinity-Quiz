package com.infinityquiz.quizModule.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz")
data class QuizEntity(
    @PrimaryKey
    val uuidIdentifier: String
)