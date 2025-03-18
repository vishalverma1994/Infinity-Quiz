package com.infinityquiz.quizModule.data.di


import android.content.Context
import androidx.room.Room
import com.infinityquiz.quizModule.data.local.QuizDatabase
import com.infinityquiz.quizModule.util.QUIZ_DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides a singleton instance of the [QuizDatabase] using Room.
     *
     * This function is annotated with `@Provides` and `@Singleton`, indicating that it's a Dagger Hilt
     * provider method for creating and managing a single instance of the database throughout the application's lifecycle.
     *
     * @param app The application context. This is used to build the Room database.
     *            The `@ApplicationContext` qualifier is used to inject the application-level context,
     *            which is safe to use for database operations.
     * @return A singleton instance of [QuizDatabase].
     */
    @Singleton
    @Provides
    fun provideYourDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app, QuizDatabase::class.java, QUIZ_DB_NAME
    ).build()

    /**
     * Provides an instance of the [QuizDao] interface.
     *
     * This function is annotated with `@Singleton` and `@Provides`, making it a
     * dependency provider for Dagger/Hilt. It fetches the [QuizDao] instance from the
     * provided [QuizDatabase] instance.
     *
     * The [QuizDao] is responsible for data access operations related to quiz entities.
     * By providing it through Dagger, we achieve dependency injection and testability.
     *
     * @param db The [QuizDatabase] instance from which the [QuizDao] will be retrieved.
     * @return An instance of [QuizDao] that can be injected into other components.
     */
    @Singleton
    @Provides
    fun provideQuizDao(db: QuizDatabase) = db.quizDao()
}