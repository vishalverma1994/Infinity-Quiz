package com.infinityquiz.quizModule.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infinityquiz.quizModule.data.dto.QuizDto

@Dao
interface QuizDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBookmark(quiz: QuizEntity)

    @Delete
    suspend fun removeBookmark(quiz: QuizEntity)

    @Query("SELECT * FROM quiz")
    suspend fun getBookmarkQuizList(): List<QuizEntity>

    @Query("SELECT EXISTS(SELECT * FROM quiz WHERE uuidIdentifier = :questionId)")
    suspend fun isQuestionBookmarked(questionId: String):Boolean
}