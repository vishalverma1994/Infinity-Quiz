package com.infinityquiz.quizModule.domain.repository

import com.infinityquiz.quizModule.domain.model.Question
import com.infinityquiz.quizModule.util.Either
import com.infinityquiz.quizModule.util.NetworkError

interface QuizRepository {
    suspend fun bookmarkQuestion(question: Question)
    suspend fun getAllBookmarkedQuestions():List<Question>
    suspend fun unbookmarkQuestion(question: Question)
    suspend fun isQuestionBookmarked(questionId: String): Boolean
    suspend fun getQuizzes() : Either<NetworkError, List<Question>>
    // Other functions such as fetching questions from API or Room can be defined here.
}