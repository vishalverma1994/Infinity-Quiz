package com.infinityquiz.quizModule.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [QuizEntity::class], version = 1,exportSchema = false)
@TypeConverters(Converters::class)
abstract class QuizDatabase: RoomDatabase() {
    abstract fun quizDao(): QuizDao
}