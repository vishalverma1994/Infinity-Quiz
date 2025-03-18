package com.infinityquiz.quizModule.data.network

import com.infinityquiz.quizModule.data.dto.QuizDto
import retrofit2.Response
import retrofit2.http.GET

interface QuizApi {

    @GET("mcq/content")
    suspend fun getQuizList(): Response<List<QuizDto>>
}