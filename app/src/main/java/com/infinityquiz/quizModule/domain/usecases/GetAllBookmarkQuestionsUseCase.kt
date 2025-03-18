package com.infinityquiz.quizModule.domain.usecases

import com.infinityquiz.quizModule.domain.model.Question
import com.infinityquiz.quizModule.domain.repository.QuizRepository
import javax.inject.Inject

/**
 * Use case for retrieving all bookmarked questions.
 *
 * This class encapsulates the logic for fetching a list of all questions that the user has previously
 * bookmarked. It interacts with the [QuizRepository] to access the data layer.
 *
 * @property repository The repository responsible for data access related to quizzes and questions.
 *                     It must implement the [QuizRepository] interface.
 */
class GetAllBookmarkQuestionsUseCase @Inject constructor(private val repository: QuizRepository) {
    suspend operator fun invoke() : List<Question> {
       return repository.getAllBookmarkedQuestions()
    }
}

