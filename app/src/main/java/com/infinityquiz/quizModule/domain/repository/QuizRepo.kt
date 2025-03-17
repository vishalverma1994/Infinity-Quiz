package com.infinityquiz.quizModule.domain.repository

import com.infinityquiz.quizModule.data.dto.QuizDto
import com.infinityquiz.quizModule.util.Either
import com.infinityquiz.quizModule.util.NetworkError

interface QuizRepo {

    suspend fun getQuizzes(): Either<NetworkError, QuizDto>
}