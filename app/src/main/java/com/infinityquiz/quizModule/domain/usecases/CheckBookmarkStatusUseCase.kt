package com.infinityquiz.quizModule.domain.usecases

import com.infinityquiz.quizModule.domain.repository.QuizRepository
import javax.inject.Inject

/**
 * Use case for checking the bookmark status of a specific question.
 *
 * This class encapsulates the logic for determining whether a question with a given ID
 * has been bookmarked by the user. It interacts with the [QuizRepository] to retrieve
 * the bookmark status from the data layer.
 *
 * @property repository The [QuizRepository] instance used to access and manage question bookmark data.
 */
class CheckBookmarkStatusUseCase @Inject constructor(private val repository: QuizRepository) {
    suspend operator fun invoke(questionId: String): Boolean {
        return repository.isQuestionBookmarked(questionId)
    }
}