package com.infinityquiz.quizModule.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinityquiz.R
import com.infinityquiz.quizModule.util.NavigationModeUtil
import com.infinityquiz.quizModule.util.startMusic
import com.infinityquiz.quizModule.util.stopMusic


/**
 * [QuizScreen] is a composable function that displays the quiz interface to the user.
 * It manages the quiz flow, including fetching questions, handling timer, user interactions,
 * and navigating between different states of the quiz.
 *
 * @param timer The initial time allocated for the quiz, in seconds.
 * @param mode The mode of the quiz, which can be "START" for a new quiz or
 *             any other value to indicate retrieval of bookmarked questions.
 *             Determined by [NavigationModeUtil].
 * @param viewModel The [QuizViewModel] instance used to manage the quiz state and logic.
 *                  Defaults to a [hiltViewModel] instance.
 * @param onQuizFinished A lambda function invoked when the quiz is finished.
 *                       It provides the total score and the number of questions presented to the user.
 *                       Defaults to an empty lambda.
 *
 * The screen handles the following states:
 * - **Delight:** Displays a screen when the user reaches a certain milestone.
 * - **Finished:** Displays when the quiz is over and provides the final result.
 * - **Question:** Displays the current question, options, timer, and interactive elements.
 *
 *  It handles:
 *  - Music playback during the quiz and control through a toggle button.
 *  - Question fetching based on the selected mode.
 */
@Composable
fun QuizScreen(
    timer: Int,
    mode: String,
    viewModel: QuizViewModel = hiltViewModel<QuizViewModel>(),
    onQuizFinished: (Int, Int) -> Unit = { _, _ -> },
) {
    val context = LocalContext.current
    val updatedTimer by viewModel.timerFlow.collectAsState()
    val quizState by viewModel.quizState.collectAsState()
    val isBookmarked by viewModel.isBookmarked.collectAsState()
    val totalScore = viewModel.totalScore.collectAsState()
    var isMusicPlaying by remember { mutableStateOf(true) }

    DisposableEffect(Unit) {
        onDispose {
            stopMusic(context)
        }
    }

    LaunchedEffect(mode, timer) {
        if (mode == NavigationModeUtil.START.name) {
            viewModel.fetchQuestionsFromApi()
        } else {
            viewModel.fetchBookmarkedQuestions()
        }
        viewModel.startTimer(timer)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        when (quizState.currentScreen) {
            QuizScreenState.Delight -> {
                LaunchedEffect(Unit) {
                    stopMusic(context)
                    isMusicPlaying = false
                }
                DelightScreen(totalScore.value, viewModel.currentIndex) {
                    viewModel.moveToNextQuestion(timer, true)
                }
            }

            QuizScreenState.Finished -> {
                LaunchedEffect(Unit) {
                    stopMusic(context)
                    isMusicPlaying = false
                    onQuizFinished(totalScore.value, viewModel.currentIndex)
                }
            }

            else -> {
                LaunchedEffect(Unit) {
                    if (isMusicPlaying)
                        startMusic(context)
                }

                // Music Toggle Button inside the else part
                IconButton(
                    onClick = {
                        isMusicPlaying = !isMusicPlaying
                        if (isMusicPlaying) {
                            startMusic(context)
                        } else {
                            stopMusic(context)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(vertical = 20.dp)
                        .padding(16.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            if (isMusicPlaying) {
                                R.drawable.ic_volume_up
                            } else {
                                R.drawable.ic_volume_mute
                            }
                        ),
                        contentDescription = "Toggle Music",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                QuestionScreen(
                    currentScreen = quizState.currentScreen,
                    isUserSelectedAnswerCorrect = quizState.isAnswerCorrect,
                    question = quizState.currentQuestion,
                    onAnswerSelected = { selectedOption -> viewModel.selectAnswer(selectedOption) },
                    onSkip = { viewModel.moveToNextQuestion(timer, false) },
                    onBookmarkToggle = { viewModel.toggleBookmark() },
                    isBookmarked = isBookmarked, updatedTimer,
                    selectedOption = quizState.selectedOption,
                    onSoundPlay = { isPlaying ->
                        if (isMusicPlaying) {
                            if (isPlaying)
                                startMusic(context)
                            else stopMusic(context)
                        }
                    }
                )
            }
        }
    }
}




