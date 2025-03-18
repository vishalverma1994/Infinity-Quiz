package com.infinityquiz.quizModule.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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


/**
 * [CountrySelectionSection] is a composable function that displays a section for the user to choose their country.
 *
 * This section includes:
 *  - A title: "Choose your country"
 *  - A [CountryPicker] component to allow country selection.
 *
 * @param modifier The modifier to be applied to the layout.
 *
 */
@Composable
fun CountrySelectionSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Column {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Choose your country",
                fontSize = 18.sp,
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
        CountrySelectionSection()
    }
}