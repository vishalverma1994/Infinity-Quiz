package com.infinityquiz.quizModule.data.dto

import com.google.gson.annotations.SerializedName
import com.infinityquiz.quizModule.domain.model.Content
import com.infinityquiz.quizModule.domain.model.Question


data class QuizDto(

    @SerializedName("uuidIdentifier") var uuidIdentifier: String? = null,
    @SerializedName("questionType") var questionType: String? = null,
    @SerializedName("question") var question: String? = null,
    @SerializedName("option1") var option1: String? = null,
    @SerializedName("option2") var option2: String? = null,
    @SerializedName("option3") var option3: String? = null,
    @SerializedName("option4") var option4: String? = null,
    @SerializedName("correctOption") var correctOption: Int? = null,
    @SerializedName("sort") var sort: Int? = null,
    @SerializedName("solution") var solutionDto: ArrayList<SolutionDto> = arrayListOf()

)

fun QuizDto.toQuestion(): Question {
    return Question(
        uuidIdentifier = uuidIdentifier ?: "",
        questionType = questionType ?: "",
        question = question ?: "",
        option1 = option1 ?: "",
        option2 = option2 ?: "",
        option3 = option3 ?: "",
        option4 = option4 ?: "",
        correctOption = correctOption ?: 0,
        solution = solutionDto.map {
            Content(contentType = it.contentType ?: "", contentData = it.contentData ?: "")
        })
}


