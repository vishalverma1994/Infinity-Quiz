package com.infinityquiz.quizModule.presentation.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.infinityquiz.quizModule.presentation.component.CountrySelectionSection
import com.infinityquiz.quizModule.util.NavigationModeUtil
import com.infinityquiz.ui.theme.InfinityQuizTheme

/**
 * [HomeScreen] is the main screen of the app.
 * This composable function shows Country Selection Section and Time Selection Dropdown.
 * It also has a Start Quiz Button and a Solve Bookmarks Button.
 * Start Quiz Button navigates to Quiz Screen and Solve Bookmarks Button navigates to Bookmark Screen.
 *
 * @param modifier Modifier to apply to this layout.
 * @param onQuizStart Callback to be invoked when the Start Quiz button is clicked.
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onQuizStart: (String, Int) -> Unit = { _, _ -> },
) {
    var selectedTime by remember { mutableIntStateOf(30) }
    val timeOptions = listOf(30, 60, 90)
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            CountrySelectionSection()

            // Time Selection Dropdown
            Column {
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = "Select Time",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(selectedTime.toString(),
                    modifier = Modifier
                        .fillMaxWidth().border(width = 2.dp, Color.Black).padding(8.dp)
                        .clickable { expanded = !expanded }
                )

                // Dropdown menu
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    timeOptions.forEach { time ->
                        DropdownMenuItem(text = {
                            Text(text = "$time seconds")
                        }, onClick = {
                            selectedTime = time
                            expanded = false
                        })
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp)
                .padding(bottom = 60.dp)
        ) {
            Button(
                onClick = { onQuizStart.invoke(NavigationModeUtil.START.name, selectedTime) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Start Quiz")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { onQuizStart.invoke(NavigationModeUtil.BOOKMARK.name, selectedTime) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Solve Bookmarks")
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    InfinityQuizTheme {
        HomeScreen()
    }
}