package com.infinityquiz.quizModule.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * The Room database for storing quiz data.
 *
 * This class serves as the main access point for the underlying SQLite database.
 * It defines the database configuration, including the entities (tables),
 * version, and type converters.
 *
 * @property entities An array of [Class] objects representing the entities (tables)
 *                   in the database. In this case, it's [QuizEntity::class].
 * @property version The version number of the database. Incrementing this value
 *                  triggers a database migration if changes are made to the
 *                  database schema. Here, it's version 1.
 * @property exportSchema Whether or not to export the database schema to a JSON
 *                       file. Here it's set to `false` which is typically recommended for
 *                       production builds. If set to true it allows to track database schema changes.
 * @property typeConverters An array of converter classes that are used for converting
 *                           custom types to types that can be stored in the database
 *                           (and vice versa). Here it uses [Converters::class]
 *                           to allow storing custom data types.
 */
@Database(entities = [QuizEntity::class], version = 1,exportSchema = false)
@TypeConverters(Converters::class)
abstract class QuizDatabase: RoomDatabase() {
    abstract fun quizDao(): QuizDao
}