package com.infinityquiz.quizModule.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.infinityquiz.quizModule.domain.model.Content
import com.infinityquiz.quizModule.domain.model.Question

@Entity(tableName = "quiz")
@TypeConverters(Converters::class)
data class QuizEntity(
    @PrimaryKey
    val uuidIdentifier: String,
    val questionType: String,
    val question: String,
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String,
    val correctOption: Int,
    val solution: List<Content>
)

fun Question.toQuizEntity(): QuizEntity {
    return QuizEntity(
        uuidIdentifier = uuidIdentifier,
        questionType = questionType,
        question = question,
        option1 = option1,
        option2 = option2,
        option3 = option3,
        option4 = option4,
        correctOption = correctOption,
        solution = solution
    )
}

fun QuizEntity.toQuestion(): Question {
    return Question(
        uuidIdentifier = uuidIdentifier,
        questionType = questionType,
        question = question,
        option1 = option1,
        option2 = option2,
        option3 = option3,
        option4 = option4,
        correctOption = correctOption,
        solution = solution
    )
}
