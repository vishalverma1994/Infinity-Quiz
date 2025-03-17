package com.infinityquiz.quizModule.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface QuizDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(quizList: List<QuizEntity>)

    @Query("SELECT * FROM quiz")
    suspend fun getAllQuiz(): List<QuizEntity>

    @Transaction
    suspend fun updateAndGetQuiz(exchangeRates: List<QuizEntity>): List<QuizEntity> {
        insertAll(exchangeRates)
        return getAllQuiz()
    }
}