package com.infinityquiz

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.infinityquiz.quizModule.presentation.screens.DelightScreen
import com.infinityquiz.quizModule.presentation.screens.HomeScreen
import com.infinityquiz.quizModule.presentation.screens.QuizScreen
import com.infinityquiz.quizModule.presentation.screens.QuizViewModel
import com.infinityquiz.quizModule.presentation.screens.ScoreScreen
import com.infinityquiz.ui.theme.InfinityQuizTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InfinityQuizTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    App(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun App(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    QuizNavHost(navController = navController)
}

/**
 * [QuizNavHost] is the main navigation graph for the quiz application.
 *
 * It defines the navigation destinations and transitions between the different screens
 * of the application using Jetpack Compose Navigation.
 *
 * The navigation graph consists of the following routes:
 * - [HomeRoute]: The starting screen where users can select the quiz mode and time.
 * - [QuizRoute]: The screen where the quiz questions are displayed.
 * - [DelightRoute]: A screen to display a delight animation
 * - [ScoreRoute]: The screen where the user's quiz score is displayed.
 *
 * @param navController The [NavHostController] that manages the navigation state.
 */
@Composable
fun QuizNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = HomeRoute) {
        composable<HomeRoute> {

            HomeScreen { mode, time ->
                //navigate to quiz screen using mode
                navController.navigate(QuizRoute(mode, time))
            }
        }
        composable<QuizRoute> { backStackEntry ->

            val quizViewModel: QuizViewModel = hiltViewModel()
            val lifecycleOwner = LocalLifecycleOwner.current.lifecycle
            val context = LocalContext.current

            LaunchedEffect(key1 = Unit) {
                lifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                    quizViewModel.errorEvent.collect {
                        // using this immediate dispatcher to avoid loosing of state send by channel
                        withContext(Dispatchers.Main.immediate) {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            val quizRoute: QuizRoute = backStackEntry.toRoute()
            // mode: "start" for API quiz "bookmark" for bookmarked questions.
            QuizScreen(
                mode = quizRoute.mode,
                timer = quizRoute.time,
                onQuizFinished = { correctScore, totalScore ->
                    //navigate to score screen
                    navController.navigate(ScoreRoute(correctScore = correctScore, totalScore = totalScore))
                })
        }
        composable<DelightRoute> { backStackEntry ->
            val delightRoute: DelightRoute = backStackEntry.toRoute()
            DelightScreen(delightRoute.correctScore, delightRoute.totalScore) {
                navController.popBackStack()
            }
        }
        composable<ScoreRoute> { backStackEntry ->
            val scoreRoute: ScoreRoute = backStackEntry.toRoute()
            ScoreScreen(correctScore = scoreRoute.correctScore, totalQuestions = scoreRoute.totalScore, onContinueClick = {
                val homeRoute: HomeRoute = backStackEntry.toRoute()
                navController.navigate(homeRoute)
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    InfinityQuizTheme {
        App()
    }
}