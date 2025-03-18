package com.infinityquiz.quizModule.domain.usecases

import com.infinityquiz.quizModule.domain.model.Question
import com.infinityquiz.quizModule.domain.repository.QuizRepository
import com.infinityquiz.quizModule.util.Either
import com.infinityquiz.quizModule.util.NetworkError
import javax.inject.Inject

class GetQuizzesUseCase @Inject constructor(private val repository: QuizRepository) {
    suspend operator fun invoke(): Either<NetworkError, List<Question>> {
        return repository.getQuizzes()
    }
}