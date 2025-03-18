package com.infinityquiz.quizModule.presentation.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.infinityquiz.quizModule.domain.model.Question
import com.infinityquiz.quizModule.domain.usecases.BookmarkQuestionUseCase
import com.infinityquiz.quizModule.domain.usecases.CheckBookmarkStatusUseCase
import com.infinityquiz.quizModule.domain.usecases.GetAllBookmarkQuestionsUseCase
import com.infinityquiz.quizModule.domain.usecases.GetQuizzesUseCase
import com.infinityquiz.quizModule.domain.usecases.UnbookmarkQuestionUseCase
import com.infinityquiz.quizModule.util.Either
import com.infinityquiz.quizModule.util.NetworkError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

//Screen State class
enum class QuizScreenState { Question, AnswerExplanation, Finished }

//State class
data class QuizState(
    val currentScreen: QuizScreenState = QuizScreenState.Question,
    val currentQuestion: Question? = null,
    val isAnswerCorrect: Boolean = false,
    val selectedOption: Int = 0
)

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getQuizzesUseCase: GetQuizzesUseCase,
    private val bookmarkQuestionUseCase: BookmarkQuestionUseCase,
    private val unbookmarkQuestionUseCase: UnbookmarkQuestionUseCase,
    private val checkBookmarkStatusUseCase: CheckBookmarkStatusUseCase,
    private val getBookmarkedQuestionsUseCase: GetAllBookmarkQuestionsUseCase
) : ViewModel() {
    private val TAG = "quizViewModel"

    private val _errorEvent = Channel<String>()
    val errorEvent = _errorEvent.receiveAsFlow()

    private val _quizState = MutableStateFlow(QuizState())
    val quizState: StateFlow<QuizState> = _quizState.asStateFlow()

    private val _totalScore = MutableStateFlow(0)
    val totalScore: StateFlow<Int> = _totalScore.asStateFlow()

    // Track the bookmark state for the current question
    private val _isBookmarked = MutableStateFlow(false)
    val isBookmarked: StateFlow<Boolean> = _isBookmarked.asStateFlow()
    private var questions: ImmutableList<Question> = emptyList<Question>().toImmutableList()
    var currentIndex = 0

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        // Handle exception
        // we can use this to send crashlytics logs or events
        // further error handling can be done here
        Log.e(TAG, "Handle uncaught  $exception in CoroutineExceptionHandler")
    }


    fun fetchQuestionsFromApi() {
        // Simulated API call
        viewModelScope.launch(coroutineExceptionHandler) {
            try {
                when (val result: Either<NetworkError, List<Question>> = getQuizzesUseCase.invoke()) {
                    is Either.Failure -> handleErrorCase(result.error)
                    is Either.Success -> handleSuccessCase(result.data.toImmutableList())
                }
            } catch (e: Exception) {
                if (e is CancellationException)
                    throw e

                Log.e(TAG, "handleEvent: ${e.message}")
                _errorEvent.send("Something went wrong, Please try again")
            }
        }

        viewModelScope.launch {
            if (questions.isNotEmpty())
                _isBookmarked.value = checkBookmarkStatusUseCase(questions.first().uuidIdentifier)
        }
    }

    fun fetchBookmarkedQuestions() {
        viewModelScope.launch {
            val bookmarkedQuestions: List<Question> = getBookmarkedQuestionsUseCase.invoke()
            questions = bookmarkedQuestions.toImmutableList()
            currentIndex = 0
            _quizState.value = QuizState(
                currentScreen = QuizScreenState.Question,
                currentQuestion = questions.first()
            )
            if (questions.isNotEmpty())
                _isBookmarked.value = checkBookmarkStatusUseCase(questions.first().uuidIdentifier)
        }
    }

    fun selectAnswer(selectedOption: Int) {
        val currentQuestion = questions.getOrNull(currentIndex)
        val isCorrect = currentQuestion?.correctOption == selectedOption
        if (isCorrect) {
            _totalScore.value += 1
        }
        _quizState.value = _quizState.value.copy(
            currentScreen = QuizScreenState.AnswerExplanation,
            isAnswerCorrect = isCorrect,
            selectedOption = selectedOption
        )
        stopTimer()
    }

    fun moveToNextQuestion(timer: Int) {
        stopTimer()
        startTime(timer)
        currentIndex++
        if (currentIndex < questions.size) {
            val nextQuestion = questions[currentIndex]
            _quizState.value = QuizState(
                currentScreen = QuizScreenState.Question,
                currentQuestion = nextQuestion
            )
            viewModelScope.launch {
                if (questions.isNotEmpty())
                    _isBookmarked.value = checkBookmarkStatusUseCase(nextQuestion.uuidIdentifier)
            }
        } else {
            _quizState.value = _quizState.value.copy(currentScreen = QuizScreenState.Finished)
        }
    }

    fun toggleBookmark() {
        val currentQuestion = questions.getOrNull(currentIndex) ?: return
        viewModelScope.launch {
            if (_isBookmarked.value) {
                unbookmarkQuestionUseCase(currentQuestion)
                _isBookmarked.value = false
            } else {
                bookmarkQuestionUseCase(currentQuestion)
                _isBookmarked.value = true
            }
        }
    }

    private fun handleSuccessCase(quizList: ImmutableList<Question>) {
        questions = quizList
        currentIndex = 0
        _quizState.value = QuizState(
            currentScreen = QuizScreenState.Question,
            currentQuestion = questions.first()
        )
    }

    private fun handleErrorCase(error: NetworkError) {
        viewModelScope.launch {
            when (error) {
                NetworkError.EmptyDataFound -> _errorEvent.send("No quiz found, Please try again")
                NetworkError.NoBodyFound -> _errorEvent.send("No Data found, Please try again")
                NetworkError.NoInternet -> _errorEvent.send("No Internet found, Please try again")
                is NetworkError.ServerError -> _errorEvent.send("server error, Please try again")
                is NetworkError.UnknownError -> _errorEvent.send("Something went wrong, Please try again")
            }

        }
    }

    private val _timerFlow = MutableStateFlow(0) // Current timer value in seconds
    val timerFlow: StateFlow<Int> = _timerFlow.asStateFlow()

    private var isRunning = false
    private var startTimeJob: Job = Job()
    fun startTime(timer: Int) {
        if (startTimeJob.isActive)
            startTimeJob.cancel()
        startTimeJob = viewModelScope.launch {
            if (!isRunning) {
                isRunning = true
                _timerFlow.value = timer // Set the initial countdown value

                // Coroutine scope to start the timer countdown
                var timeRemaining = timer
                while (isActive && isRunning && timeRemaining > 0) {
                    delay(1000L) // Wait for 1 second
                    timeRemaining -= 1 // Decrement the timer
                    _timerFlow.value = timeRemaining // Update the time
                }
                if (timeRemaining == 0) {
                    stopTimer() // Stop when the timer hits zero
                }


            }
        }

    }

    fun stopTimer() {
        isRunning = false
//        resetTimer()
    }

    // Resets the timer
    fun resetTimer(timer: Int = 0) {
        _timerFlow.value = timer
    }
}
