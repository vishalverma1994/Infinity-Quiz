package com.infinityquiz.quizModule.presentation.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

//Screen State class
enum class QuizScreenState { Question, AnswerExplanation, Delight, Finished }

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


    /**
     * Fetches questions from the API and handles the result.
     *
     * This function initiates a request to retrieve a list of questions from an API endpoint
     * using the `getQuizzesUseCase`. It manages the response using a `Either` type,
     * which allows for handling success and failure cases separately.
     *
     * **Success Case:**
     * - If the API call is successful, the function receives a `List<Question>`.
     * - It converts this list to an immutable list using `toImmutableList()`.
     * - It then passes the immutable list to the `handleSuccessCase` function for further processing.
     *
     * **Failure Case:**
     * - If the API call fails, the function receives a `NetworkError`.
     * - It passes the `NetworkError` to the `handleErrorCase` function for appropriate handling.
     *
     * **Error Handling:**
     * - The function includes a `try-catch` block to handle any unexpected exceptions during the process.
     * - If a `CancellationException` is caught, it rethrows it to respect coroutine cancellation.
     * - For other exceptions, it logs an error message and sends a generic error message to the UI via the `_errorEvent` channel.
     *
     * **Bookmark Status Check:**
     * - After fetching the questions, it also checks the bookmark status of the first question (if any) using `checkBookmarkStatusUseCase`.
     * - The result is then updated in the `_isBookmarked` LiveData/StateFlow.
     *
     * **Concurrency:**
     * - The API call and the bookmark status check are performed in separate coroutines within the `viewModelScope`.
     * - This ensures that the bookmark check doesn't block the API call's response handling.
     *
     * **Dependencies:**
     * - `getQuizzesUseCase`: A use case responsible for fetching quizzes (questions) from the API.
     * - `handleSuccessCase`: A function that processes the successfully retrieved list of questions.
     * - `handleErrorCase`: A function that handles errors received from the API.
     */
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

    /**
     * Fetches the list of bookmarked questions from the data source, updates the UI state,
     * and initializes the quiz to the first bookmarked question if any exist.
     */
    fun fetchBookmarkedQuestions() {
        viewModelScope.launch {
            val bookmarkedQuestions: List<Question> = getBookmarkedQuestionsUseCase.invoke()
            questions = bookmarkedQuestions.toImmutableList()
            currentIndex = 0
            if (questions.isNotEmpty()) {
                _quizState.value = QuizState(
                    currentScreen = QuizScreenState.Question,
                    currentQuestion = questions.first()
                )
                _isBookmarked.value = checkBookmarkStatusUseCase(questions.first().uuidIdentifier)
            } else {
                handleErrorCase(NetworkError.NoBodyFound)
            }
        }
    }

    /**
     * Selects an answer for the current question in the quiz.
     */
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

    /**
     * Moves the quiz to the next question or the final screen.
     *
     * This function handles the logic for advancing the user through the quiz. It stops the current timer,
     * starts a new timer (if provided), and determines if a new question should be displayed, a "delight" screen,
     * or the final "finished" screen.
     *
     * @param timer The duration (in seconds) for the timer for the next question.
     * @param isFromDelight A boolean indicating whether the transition is coming from a "delight" screen.
     *                      If true, it means the user was just on a "delight" screen and we should NOT increment the `currentIndex`
     *                      or show the delight screen again.
     *
     */
    fun moveToNextQuestion(timer: Int, isFromDelight: Boolean) {
        stopTimer()
        startTimer(timer)
        if (isFromDelight.not())
            currentIndex++
        if (currentIndex < questions.size) {
            if ((currentIndex % 5) == 0 && isFromDelight.not()) {
                _quizState.value = _quizState.value.copy(
                    currentScreen = QuizScreenState.Delight
                )
            } else {
                val nextQuestion = questions[currentIndex]
                _quizState.value = _quizState.value.copy(
                    currentScreen = QuizScreenState.Question,
                    currentQuestion = nextQuestion
                )
                viewModelScope.launch {
                    if (questions.isNotEmpty())
                        _isBookmarked.value = checkBookmarkStatusUseCase(nextQuestion.uuidIdentifier)
                }
            }
        } else {
            _quizState.value = _quizState.value.copy(currentScreen = QuizScreenState.Finished)
        }
    }

    /**
     * Toggles the bookmark status of the currently displayed question.
     *
     * This function checks if the current question is bookmarked. If it is, it removes the bookmark.
     * If it's not, it adds a bookmark. It then updates the [_isBookmarked] State Flow to reflect
     * the change.
     *
     * This function internally uses coroutines within the viewModelScope to perform the bookmarking/unbookmarking
     * operations asynchronously, ensuring it does not block the main thread.
     *
     * The function handles the case where there is no current question gracefully by simply returning.
     */
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

    /**
     * Handles the successful retrieval of the quiz questions.
     *
     * This function is called when the quiz questions have been successfully fetched.
     * It updates the internal state of the quiz, including:
     *   - Storing the fetched list of questions.
     *   - Resetting the current question index to the beginning.
     *   - Updating the `_quizState` LiveData to reflect the new state,
     *     specifically setting the current screen to the question view and
     *     loading the first question from the list.
     *
     * @param quizList The immutable list of `Question` objects representing the quiz.
     *                 This list is assumed to be non-empty.
     */
    private fun handleSuccessCase(quizList: ImmutableList<Question>) {
        questions = quizList
        currentIndex = 0
        _quizState.value = QuizState(
            currentScreen = QuizScreenState.Question,
            currentQuestion = questions.first()
        )
    }

    /**
     * Handles different types of network errors and sends appropriate error messages to the UI.
     *
     * This function takes a [NetworkError] as input and, based on the specific error type,
     * sends a corresponding user-friendly error message via the `_errorEvent` channel.
     * The messages are intended to inform the user about the nature of the network issue and
     * prompt them to retry or take other relevant actions.
     *
     * @param error The [NetworkError] that occurred.
     */
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

    /**
     * Starts a countdown timer.
     *
     * This function initiates a timer that counts down from the specified `timer` value.
     * It uses coroutines to perform the countdown asynchronously without blocking the main thread.
     *
     * The timer updates the `_timerFlow` state flow with the remaining time each second.
     *
     * If a timer is already running when this function is called, the existing timer is cancelled
     * before starting the new one.
     *
     * The timer continues to decrement until one of the following conditions is met:
     * - `isActive` (the coroutine is cancelled) becomes false.
     * - `isRunning` becomes false.
     * - `timeRemaining` reaches 0.
     *
     * When `timeRemaining` reaches 0, the `stopTimer()` function is called, and `moveToNextQuestion()`
     * is called to proceed to the next question.
     *
     * @param timer The initial duration of the timer in seconds.
     */
    fun startTimer(timer: Int) {
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
                    moveToNextQuestion(timer = timer, isFromDelight = false)
                }
            }
        }
    }

    // Stops the timer
    private fun stopTimer() {
        isRunning = false
    }

    // Resets the timer
    private fun resetTimer(timer: Int = 0) {
        _timerFlow.value = timer
    }
}
