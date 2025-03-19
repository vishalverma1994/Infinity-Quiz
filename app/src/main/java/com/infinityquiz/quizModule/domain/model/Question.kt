package com.infinityquiz.quizModule.domain.model

/**
 * Domain Entity - Follow SRP for solid principles
 * Represents a single question in a quiz or assessment.
 *
 * This data class encapsulates all the necessary information for a question,
 * including its unique identifier, type, text, options, correct answer,
 * solution details, and sorting order.
 */
data class Question(
    val uuidIdentifier: String,
    val questionType: String,
    val question: String,
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String,
    val correctOption: Int,
    val solution: List<Content>,
    val sort: Int,
)

data class Content(
    val contentType: String,
    val contentData: String
)
