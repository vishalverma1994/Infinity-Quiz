package com.infinityquiz.quizModule.domain.usecases

import com.infinityquiz.quizModule.domain.model.Question
import com.infinityquiz.quizModule.domain.repository.QuizRepository
import javax.inject.Inject

// Use case to remove a bookmark
class UnbookmarkQuestionUseCase @Inject constructor(private val repository: QuizRepository) {
    suspend operator fun invoke(question: Question) {
        repository.unbookmarkQuestion(question)
    }
}