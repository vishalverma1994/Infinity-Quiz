package com.infinityquiz.quizModule.domain.repository

import com.infinityquiz.quizModule.domain.model.Question
import com.infinityquiz.quizModule.util.Either
import com.infinityquiz.quizModule.util.NetworkError

/**
 * Interface for managing quiz data and operations related to questions,
 * including bookmarking, retrieving bookmarked questions, and fetching quizzes.
 */
interface QuizRepository {
    suspend fun bookmarkQuestion(question: Question)
    suspend fun getAllBookmarkedQuestions():List<Question>
    suspend fun unbookmarkQuestion(question: Question)
    suspend fun isQuestionBookmarked(questionId: String): Boolean
    suspend fun getQuizzes() : Either<NetworkError, List<Question>>
}