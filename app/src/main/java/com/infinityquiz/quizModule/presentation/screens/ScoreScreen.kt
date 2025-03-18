package com.infinityquiz.quizModule.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

/**
 * Displays the score screen after a quiz or game session.
 *
 * This composable shows the user's score, including the number of correct answers
 * out of the total number of questions. It also displays a celebratory animation
 * and a "Continue" button to proceed.
 *
 * @param correctScore The number of correct answers.
 * @param totalQuestions The total number of questions.
 * @param onContinueClick Callback function to be invoked when the "Continue" button is clicked.
 */
@Composable
fun ScoreScreen(correctScore: Int, totalQuestions: Int, onContinueClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.Asset("complete_celebration.json"))
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            isPlaying = true
        )

        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Awesome you got", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(40.dp))

        Row {
            Text(
                text = "$correctScore",
                style = MaterialTheme.typography.displayMedium.copy(color = Color.Green),
                modifier = Modifier.alignByBaseline()
            )
            Text(
                text = "/$totalQuestions",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                modifier = Modifier.alignByBaseline()
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Correct", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onContinueClick) {
            Text(text = "Continue")
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ScoreScreenPreview() {
    ScoreScreen(onContinueClick = {}, correctScore = 2, totalQuestions = 5)
}