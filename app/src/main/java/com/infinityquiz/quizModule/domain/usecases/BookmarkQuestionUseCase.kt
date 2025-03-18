package com.infinityquiz.quizModule.domain.usecases

import com.infinityquiz.quizModule.domain.model.Question
import com.infinityquiz.quizModule.domain.repository.QuizRepository
import javax.inject.Inject

/**
 * Use case responsible for bookmarking a question.
 *
 * This class encapsulates the logic for adding a question to the user's bookmarks.
 * It interacts with the [QuizRepository] to persist the bookmarking operation.
 *
 * @property repository The repository used to access and modify quiz data, including bookmarks.
 */
class BookmarkQuestionUseCase @Inject constructor(private val repository: QuizRepository) {
    suspend operator fun invoke(question: Question) {
        repository.bookmarkQuestion(question)
    }
}

