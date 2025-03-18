package com.infinityquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.infinityquiz.quizModule.presentation.screens.HomeScreen
import com.infinityquiz.quizModule.presentation.screens.QuizScreen
import com.infinityquiz.quizModule.presentation.screens.ScoreScreen
import com.infinityquiz.ui.theme.InfinityQuizTheme
import dagger.hilt.android.AndroidEntryPoint

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

@Composable
fun QuizNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = HomeRoute) {
        composable<HomeRoute> {

            HomeScreen { mode ->
                //navigate to quiz screen using mode
                navController.navigate(QuizRoute(mode))
            }
        }
        composable<QuizRoute> { backStackEntry ->
            val quizRoute: QuizRoute = backStackEntry.toRoute()
            // mode: "start" for API quiz "bookmark" for bookmarked questions.
            QuizScreen(mode = quizRoute.mode) { correctScore, totalScore ->
                //navigate to score screen
                navController.navigate(ScoreRoute(correctScore = correctScore, totalScore = totalScore))
            }
        }
        composable<ScoreRoute> { backStackEntry ->
            val scoreRoute: ScoreRoute = backStackEntry.toRoute()
            ScoreScreen(correctScore = scoreRoute.correctScore, totalScore = scoreRoute.totalScore, onContinueClick = {
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