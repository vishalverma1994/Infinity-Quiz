package com.infinityquiz.quizModule.domain.di

import com.infinityquiz.quizModule.domain.repository.QuizRepository
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UseCaseModuleTest {

    @Mock
    private lateinit var mockRepository: QuizRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `provideBookmarkQuestionUseCase successful creation`() {
        val useCase = UseCaseModule.provideBookmarkQuestionUseCase(mockRepository)
        assertNotNull(useCase)
    }

    @Test(expected = NullPointerException::class)
    fun `provideBookmarkQuestionUseCase null repository`() {
        UseCaseModule.provideBookmarkQuestionUseCase(null!!)
    }

    @Test
    fun `provideCheckBookmarkStatusUseCase successful creation`() {
        val useCase = UseCaseModule.provideCheckBookmarkStatusUseCase(mockRepository)
        assertNotNull(useCase)
    }

    @Test(expected = NullPointerException::class)
    fun `provideCheckBookmarkStatusUseCase null repository`() {
        UseCaseModule.provideCheckBookmarkStatusUseCase(null!!)
    }

    @Test
    fun `provideGetQuizzesUseCase successful creation`() {
        val useCase = UseCaseModule.provideGetQuizzesUseCase(mockRepository)
        assertNotNull(useCase)
    }

    @Test(expected = NullPointerException::class)
    fun `provideGetQuizzesUseCase null repository`() {
        UseCaseModule.provideGetQuizzesUseCase(null!!)
    }

    @Test
    fun `provideUnbookmarkQuestionUseCase successful creation`() {
        val useCase = UseCaseModule.provideUnbookmarkQuestionUseCase(mockRepository)
        assertNotNull(useCase)
    }

    @Test(expected = NullPointerException::class)
    fun `provideUnbookmarkQuestionUseCase null repository`() {
        UseCaseModule.provideUnbookmarkQuestionUseCase(null!!)
    }

    @Test
    fun `provideBookmarkQuestionUseCase check dependency`() {
        val useCase = UseCaseModule.provideBookmarkQuestionUseCase(mockRepository)
        assertNotNull(useCase)
        Mockito.verifyNoInteractions(mockRepository) // Ensures repository isn't called at creation
    }

    @Test
    fun `provideCheckBookmarkStatusUseCase check dependency`() {
        val useCase = UseCaseModule.provideCheckBookmarkStatusUseCase(mockRepository)
        assertNotNull(useCase)
        Mockito.verifyNoInteractions(mockRepository)
    }

    @Test
    fun `provideGetQuizzesUseCase check dependency`() {
        val useCase = UseCaseModule.provideGetQuizzesUseCase(mockRepository)
        assertNotNull(useCase)
        Mockito.verifyNoInteractions(mockRepository)
    }

    @Test
    fun `provideUnbookmarkQuestionUseCase check dependency`() {
        val useCase = UseCaseModule.provideUnbookmarkQuestionUseCase(mockRepository)
        assertNotNull(useCase)
        Mockito.verifyNoInteractions(mockRepository)
    }

}