package com.infinityquiz.quizModule.domain.model

// Domain Entity
data class Question(
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

data class Content(
    val contentType: String,
    val contentData: String
)
