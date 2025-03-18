package com.infinityquiz.quizModule.presentation.screens

import android.media.MediaPlayer
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.infinityquiz.R

/**
 * DelightScreen: A composable screen to celebrate the user's progress in a quiz.
 *
 * This screen displays a celebratory animation, shows the user's current score,
 * and provides a button to continue to the next part of the quiz. It also plays
 * a celebratory sound effect while the screen is active.
 *
 * @param correctCount The number of questions the user has answered correctly.
 * @param totalCount The total number of questions in the quiz.
 * @param onContinue A lambda function to be executed when the "Continue Quiz" button is clicked.
 *                   This is typically used to navigate to the next screen or part of the application.
 */
@Composable
fun DelightScreen(correctCount: Int, totalCount: Int, onContinue: () -> Unit) {
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.cracker_sound) }

    // Play the sound when the screen is displayed
    LaunchedEffect(Unit) {
        mediaPlayer.start()
        mediaPlayer.isLooping = true
    }

    // Stop the sound when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.Asset("celebration.json"))
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

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Current Progress", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Score: $correctCount / $totalCount")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onContinue() }) {
            Text("Continue Quiz")
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DelightScreenPreview() {
    DelightScreen(2, 5) { }
}