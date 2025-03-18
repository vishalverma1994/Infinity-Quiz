package com.infinityquiz.quizModule.domain.usecases

import com.infinityquiz.quizModule.domain.repository.QuizRepository
import javax.inject.Inject

// Use case to check bookmark status
class CheckBookmarkStatusUseCase @Inject constructor(private val repository: QuizRepository) {
    suspend operator fun invoke(questionId: String): Boolean {
        return repository.isQuestionBookmarked(questionId)
    }
}