package com.infinityquiz.quizModule.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.infinityquiz.quizModule.domain.model.Content
import com.infinityquiz.quizModule.domain.model.Question

/**
 * Represents a quiz question entity in the database.
 *
 * This class defines the schema for the "quiz" table in the Room database. Each instance of this class
 * represents a single quiz question, along with its details like the question text, options, correct
 * answer, and solution.
 */
@Entity(tableName = "quiz")
@TypeConverters(Converters::class)
data class QuizEntity(
    @PrimaryKey
    val uuidIdentifier: String,
    val questionType: String,
    val question: String,
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String,
    val correctOption: Int,
    val sort: Int ,
    val solution: List<Content>
)

/**
 * Converts a [Question] data class to a [QuizEntity] data class.
 *
 * This function maps the properties of a [Question] object to a corresponding [QuizEntity] object.
 * It's typically used to transform data from a domain or network layer representation
 * (represented by [Question]) to a data layer or persistence layer representation (represented by [QuizEntity]).
 *
 * @return A new [QuizEntity] object populated with the data from the [Question] object.
 */
fun Question.toQuizEntity(): QuizEntity {
    return QuizEntity(
        uuidIdentifier = uuidIdentifier,
        questionType = questionType,
        question = question,
        option1 = option1,
        option2 = option2,
        option3 = option3,
        option4 = option4,
        correctOption = correctOption,
        sort = sort,
        solution = solution
    )
}

/**
 * Converts a [QuizEntity] object to a [Question] object.
 *
 * This function maps the properties of a [QuizEntity] to the corresponding
 * properties of a [Question], effectively transforming the data structure.
 *
 * @return A [Question] object containing the data from the original [QuizEntity].
 */
fun QuizEntity.toQuestion(): Question {
    return Question(
        uuidIdentifier = uuidIdentifier,
        questionType = questionType,
        question = question,
        option1 = option1,
        option2 = option2,
        option3 = option3,
        option4 = option4,
        correctOption = correctOption,
        sort = sort,
        solution = solution
    )
}
