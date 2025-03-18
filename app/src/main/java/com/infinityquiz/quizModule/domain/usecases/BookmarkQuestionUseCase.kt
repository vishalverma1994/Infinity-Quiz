package com.infinityquiz.quizModule.domain.usecases

import com.infinityquiz.quizModule.domain.model.Question
import com.infinityquiz.quizModule.domain.repository.QuizRepository
import javax.inject.Inject

// Use case to bookmark a question

class BookmarkQuestionUseCase @Inject constructor(private val repository: QuizRepository) {
    suspend operator fun invoke(question: Question) {
        repository.bookmarkQuestion(question)
    }
}

