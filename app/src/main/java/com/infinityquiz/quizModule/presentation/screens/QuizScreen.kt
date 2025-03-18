package com.infinityquiz.quizModule.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinityquiz.quizModule.util.NavigationModeUtil

@Composable
fun QuizScreen(
    timer: Int = 62,
    mode: String,
    viewModel: QuizViewModel = hiltViewModel<QuizViewModel>(),
    onQuizFinished: (Int, Int) -> Unit
) {
    val updatedTimer by viewModel.timerFlow.collectAsState()
    val quizState by viewModel.quizState.collectAsState()
    val isBookmarked by viewModel.isBookmarked.collectAsState()

    LaunchedEffect(mode, timer) {
        if (mode == NavigationModeUtil.START.name) {
            viewModel.fetchQuestionsFromApi()
        } else {
            viewModel.fetchBookmarkedQuestions()
        }
        viewModel.startTime(timer)
    }

    QuestionScreen(
        currentScreen = quizState.currentScreen,
        isUserSelectedAnswerCorrect = quizState.isAnswerCorrect,
        question = quizState.currentQuestion,
        onAnswerSelected = { selectedOption -> viewModel.selectAnswer(selectedOption) },
        onSkip = { viewModel.moveToNextQuestion(timer) },
        onBookmarkToggle = { viewModel.toggleBookmark() },
        isBookmarked = isBookmarked,updatedTimer,
        selectedOption = quizState.selectedOption
    )
    if (quizState.currentScreen == QuizScreenState.Finished) {
//        viewModel.stopTimer()
        val totalScore = viewModel.totalScore.collectAsState()
        LaunchedEffect(Unit) {
            onQuizFinished(totalScore.value, viewModel.currentIndex)
        }
    }
}


