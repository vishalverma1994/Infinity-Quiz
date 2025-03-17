package com.infinityquiz.quizModule.presentation.screens

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject


//screen state class to handle the state of home screen
data class HomeScreenState(
    val isLoading: Boolean = false,
)

//events that are triggered from ui for viewModel
sealed interface ScreenEvent {
    data class onCountryChange(val country: String): ScreenEvent

    object GetCountry: ScreenEvent
}

@HiltViewModel
class QuizViewModel @Inject constructor(): ViewModel() {

    private val _screenState = MutableStateFlow(HomeScreenState())
    val screenState = _screenState.asStateFlow()
    private val _errorEvent = Channel<String>()
    val errorEvent = _errorEvent.receiveAsFlow()
}