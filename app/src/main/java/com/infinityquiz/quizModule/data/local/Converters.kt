package com.infinityquiz.quizModule.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.infinityquiz.quizModule.domain.model.Content

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromContentList(value: List<Content>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toContentList(value: String): List<Content> {
        val type = object : TypeToken<List<Content>>() {}.type
        return gson.fromJson(value, type)
    }
}