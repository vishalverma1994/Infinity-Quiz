package com.infinityquiz.quizModule.data.network

import com.infinityquiz.quizModule.data.dto.QuizDto
import retrofit2.Response
import retrofit2.http.GET

/**
 * Interface defining the API endpoints for interacting with quiz-related data.
 *
 * This interface uses Retrofit annotations to define the HTTP requests and their
 * corresponding endpoints. It provides a method to retrieve a list of quizzes.
 */
interface QuizApi {

    @GET("mcq/content")
    suspend fun getQuizList(): Response<List<QuizDto>>
}