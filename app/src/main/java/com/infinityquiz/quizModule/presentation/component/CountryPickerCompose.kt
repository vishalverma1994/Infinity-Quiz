package com.infinityquiz.quizModule.presentation.component

import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.hbb20.CountryCodePicker

@Composable
fun CountryPicker() {
    val context = LocalContext.current
    var selectedCountry by remember { mutableStateOf("Select Country") }

    OutlinedCard(modifier = Modifier
        .fillMaxWidth()
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
