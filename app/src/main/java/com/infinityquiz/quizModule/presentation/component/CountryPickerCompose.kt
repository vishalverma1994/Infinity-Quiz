package com.infinityquiz.quizModule.presentation.component

import android.view.ViewGroup
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.hbb20.CountryCodePicker

/**
 * A composable function that displays a country picker using the `CountryCodePicker` library.
 *
 * This function provides a UI element that allows the user to select a country from a list.
 * It utilizes the `CountryCodePicker` Android library within a Compose `AndroidView` for seamless
 * integration. The selected country's name is tracked and can be used elsewhere in the application.
 *
 * Key features:
 * - Displays a card containing the country picker.
 * - Shows the full name of the selected country.
 * - Hides the phone code, name code, flag, and auto-detection.
 * - Shows an arrow to indicate the dropdown nature of the picker.
 * - Centers the selected country's text.
 * - Tracks and updates the selected country's name.
 */
@Composable
fun CountryPicker() {
    val context = LocalContext.current
    var selectedCountry by remember { mutableStateOf("Select Country") }

    Box(modifier = Modifier
        .fillMaxWidth()
        .border(width = 2.dp, color = Color.Black)
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth(),
            factory = { ctx ->
                CountryCodePicker(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, // Set width to match parent
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    showFullName(true)
                    setShowPhoneCode(false)
                    showNameCode(false)
                    showArrow(true)
                    showFlag(false)
                    setAutoDetectedCountry(false)
                    setCurrentTextGravity(CountryCodePicker.TextGravity.CENTER)

                    // Handle country selection
                    setOnCountryChangeListener {
                        selectedCountry = selectedCountryName
                    }
                }
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun CountryPickerPreview() {
    CountryPicker()
}