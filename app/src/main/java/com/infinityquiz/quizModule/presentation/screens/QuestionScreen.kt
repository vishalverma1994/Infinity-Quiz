package com.infinityquiz.quizModule.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import com.infinityquiz.quizModule.domain.model.Question
import com.infinityquiz.quizModule.presentation.component.skipBookMarkQuestion
import com.infinityquiz.quizModule.util.shareText
import java.util.Locale

@Composable
fun QuestionScreen(
    currentScreen: QuizScreenState,
    isUserSelectedAnswerCorrect: Boolean,
    question: Question?,
    onAnswerSelected: (Int) -> Unit,
    onSkip: () -> Unit,
    onBookmarkToggle: () -> Unit,
    isBookmarked: Boolean, updatedTimer: Int,
    selectedOption: Int
) {
    val context = LocalContext.current
    if (question == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Box(
            modifier = Modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 100.dp)
                    .verticalScroll(rememberScrollState())
            ) {


                // Timer at the top
                Text(
                    text = String.format(Locale.getDefault(), "%02d:%02d", updatedTimer / 60, updatedTimer % 60),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
                // Display image based on question type
                when (question.questionType) {
                    "image" -> {
                        question.let { imageUrl ->
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = "Question Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.LightGray),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    else -> { // Display text based on question type
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val htmlQuestion = HtmlCompat.fromHtml(question.question, HtmlCompat.FROM_HTML_MODE_LEGACY)
                            Text(
                                text = htmlQuestion.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                listOf(
                    question.option1,
                    question.option2,
                    question.option3,
                    question.option4
                ).forEachIndexed { index, optionText ->
                    val isCorrect = question.correctOption == index + 1
                    val modifier = if (currentScreen == QuizScreenState.AnswerExplanation ) {
                        if (isUserSelectedAnswerCorrect.not() && selectedOption == index + 1){
                            Modifier.border(width = 2.dp, Color.Red)
                        } else if (isCorrect)
                            Modifier.border(width = 2.dp, Color.Green)
                        else Modifier
                    } else Modifier
                    Button(
                        onClick = { onAnswerSelected(index + 1) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(modifier)
                            .padding(vertical = 4.dp)
                    ) {
                        Text(text = optionText)
                    }
                }

                if (currentScreen == QuizScreenState.AnswerExplanation) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Solution :",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    AnswerExplanationScreen(solution = question.solution) {
                        shareText(
                            context = context,
                            text = question.question
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                skipBookMarkQuestion(
                    isAnswerState = currentScreen == QuizScreenState.AnswerExplanation,
                    onSkip = onSkip, onBookmarkToggle, isBookmarked
                )
            }
        }
    }
}
//
//@Preview
//@Composable
//fun QuestionScreenPreview() {
//    QuestionScreen(
//        question = null,
//        onAnswerSelected = {},
//        onSkip = {},
//        onBookmarkToggle = {},
//        isBookmarked = false
//    )
//}