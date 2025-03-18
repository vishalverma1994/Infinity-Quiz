package com.infinityquiz.quizModule.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinityquiz.quizModule.util.NavigationModeUtil

/**
 * A composable function representing the main quiz screen.
 *
 * This screen orchestrates the quiz flow, managing different states and transitions
 * between them. It fetches questions, handles user interactions, manages the timer,
 * and updates the UI accordingly.
 *
 * @param timer The duration of the timer for the quiz in seconds.
 * @param mode  The mode of the quiz, either "START" for fetching new questions or another mode for example fetching bookmarked questions. See [NavigationModeUtil].
 * @param viewModel The [QuizViewModel] instance responsible for managing the quiz's logic and state.
 *                  Defaults to an instance obtained via [hiltViewModel].
 * @param onQuizFinished A callback function invoked when the quiz is finished.
 *                       It provides the total score and the number of answered questions as parameters.
 *                       Defaults to an empty lambda.
 */
@Composable
fun QuizScreen(
    timer: Int,
    mode: String,
    viewModel: QuizViewModel = hiltViewModel<QuizViewModel>(),
    onQuizFinished: (Int, Int) -> Unit ={_,_ ->},
) {
    val updatedTimer by viewModel.timerFlow.collectAsState()
    val quizState by viewModel.quizState.collectAsState()
    val isBookmarked by viewModel.isBookmarked.collectAsState()
    val totalScore = viewModel.totalScore.collectAsState()

    LaunchedEffect(mode, timer) {
        if (mode == NavigationModeUtil.START.name) {
            viewModel.fetchQuestionsFromApi()
        } else {
            viewModel.fetchBookmarkedQuestions()
        }
        viewModel.startTimer(timer)
    }

    when (quizState.currentScreen) {
        QuizScreenState.Delight -> {
            DelightScreen(totalScore.value, viewModel.currentIndex) {
                viewModel.moveToNextQuestion(timer, true)
            }

        }
        QuizScreenState.Finished -> {
            LaunchedEffect(Unit) {
                onQuizFinished(totalScore.value, viewModel.currentIndex)
            }
        }
        else -> {
            QuestionScreen(
                currentScreen = quizState.currentScreen,
                isUserSelectedAnswerCorrect = quizState.isAnswerCorrect,
                question = quizState.currentQuestion,
                onAnswerSelected = { selectedOption -> viewModel.selectAnswer(selectedOption) },
                onSkip = { viewModel.moveToNextQuestion(timer, false) },
                onBookmarkToggle = { viewModel.toggleBookmark() },
                isBookmarked = isBookmarked, updatedTimer,
                selectedOption = quizState.selectedOption
            )
        }
    }
}



