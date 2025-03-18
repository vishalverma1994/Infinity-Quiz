package com.infinityquiz.quizModule.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.infinityquiz.ui.theme.InfinityQuizTheme


@Composable
fun CountrySelectionSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
    ) {
        Column {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Choose your country",
                fontSize = 18.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            CountryPicker()
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun CountrySelectionSectionPreview() {
    InfinityQuizTheme {
        CountrySelectionSection(

        )
    }
}