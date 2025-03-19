package com.infinityquiz.quizModule.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.infinityquiz.quizModule.domain.model.Content
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class QuizDaoTest {

    private lateinit var database: QuizDatabase
    private lateinit var quizDao: QuizDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, QuizDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        quizDao = database.quizDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun saveBookmarkValidQuiz() = runBlocking {
        val quiz = QuizEntity(
            uuidIdentifier = "q1",
            questionType = "text",
            question = "Sample Question",
            option1 = "A",
            option2 = "B",
            option3 = "C",
            option4 = "D",
            correctOption = 2,
            solution = listOf(Content("text", "explaination"))
        )
        quizDao.saveBookmark(quiz)
        val result = quizDao.getBookmarkQuizList()
        assertEquals(1, result.size)
    }

    @Test
    fun saveBookmarkDuplicateQuiz() = runBlocking {
        val quiz = QuizEntity("q1", "text", "question", "B", "C", "D", "A", 2, listOf(Content("text", "explaination")))
        quizDao.saveBookmark(quiz)
        val updatedQuiz = QuizEntity("q1", "text", "Updated Question", "A", "B", "C", "D", 3, listOf(Content("text", "explaination")))
        quizDao.saveBookmark(updatedQuiz)
        val result = quizDao.getBookmarkQuizList()
        assertEquals(1, result.size)
        assertEquals("Updated Question", result[0].question)
    }

    @Test(expected = Exception::class)
    fun saveBookmarkNullQuiz() = runBlocking {
        quizDao.saveBookmark(null!!)
    }

    @Test
    fun removeBookmarkExistingQuiz() = runBlocking {
        val quiz = QuizEntity("q1", "text", "Question", "A", "B", "C", "D", 2, listOf(Content("text", "explaination")))
        quizDao.saveBookmark(quiz)
        quizDao.removeBookmark(quiz)
        val result = quizDao.getBookmarkQuizList()
        assertTrue(result.isEmpty())
    }

    @Test
    fun removeBookmarkNonExistingQuiz() = runBlocking {
        val quiz = QuizEntity("q1", "text", "Question", "A", "B", "C", "D", 2, listOf(Content("text", "explaination")))
        quizDao.removeBookmark(quiz)
        val result = quizDao.getBookmarkQuizList()
        assertTrue(result.isEmpty())
    }

    @Test(expected = Exception::class)
    fun removeBookmarkNullQuiz() = runBlocking {
        quizDao.removeBookmark(null!!)
    }

    @Test
    fun getBookmarkQuizListEmptyList() = runBlocking {
        val result = quizDao.getBookmarkQuizList()
        assertTrue(result.isEmpty())
    }

    @Test
    fun getBookmarkQuizListMultipleQuizzes() = runBlocking {
        val quizzes = listOf(
            QuizEntity("q1", "text", "Q1", "A", "B", "C", "D", 1, listOf(Content("text", "explaination"))),
            QuizEntity("q2","text", "Q2", "A", "B", "C", "D", 2, listOf(Content("text", "explaination")))
        )
        quizzes.forEach { quizDao.saveBookmark(it) }
        val result = quizDao.getBookmarkQuizList()
        assertEquals(2, result.size)
    }

    @Test
    fun isQuestionBookmarkedBookmarkedQuestion() = runBlocking {
        val quiz = QuizEntity("q1", "text", "Q1", "A", "B", "C", "D", 1, listOf(Content("text", "explaination")))
        quizDao.saveBookmark(quiz)
        assertTrue(quizDao.isQuestionBookmarked("q1"))
    }

    @Test
    fun isQuestionBookmarkedNotBookmarkedQuestion() = runBlocking {
        assertFalse(quizDao.isQuestionBookmarked("q999"))
    }

    @Test
    fun isQuestionBookmarkedEmptyQuestionId() = runBlocking {
        assertFalse(quizDao.isQuestionBookmarked(""))
    }

    @Test(expected = Exception::class)
    fun isQuestionBookmarkedNullQuestionId(): Unit = runBlocking {
        quizDao.isQuestionBookmarked(null!!)
    }

    @Test
    fun saveBookmarkLargeQuizData() = runBlocking {
        val quiz = QuizEntity("q1", "text", "LongText".repeat(1000), "A", "B", "C", "D", 1, listOf(Content("text", "explaination")))
        quizDao.saveBookmark(quiz)
        val result = quizDao.getBookmarkQuizList()
        assertEquals(1, result.size)
    }

    @Test
    fun isQuestionBookmarkedSpecialCharacters() = runBlocking {
        val quiz = QuizEntity("q@!#%", "text", "Q Special", "A", "B", "C", "D", 2, listOf(Content("text", "explaination")))
        quizDao.saveBookmark(quiz)
        assertTrue(quizDao.isQuestionBookmarked("q@!#%"))
    }

    @Test
    fun getBookmarkQuizListLargeAmount() = runBlocking {
        val quizzes = (1..1000).map {
            QuizEntity("q$it", "text", "Q$it", "A", "B", "C", "D", it % 4, listOf(Content("text", "explaination")))
        }
        quizzes.forEach { quizDao.saveBookmark(it) }
        val result = quizDao.getBookmarkQuizList()
        assertEquals(1000, result.size)
    }
}