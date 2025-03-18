package com.infinityquiz.quizModule.data.dto

import com.google.gson.annotations.SerializedName

data class SolutionDto (
  @SerializedName("contentType" ) var contentType : String? = null,
  @SerializedName("contentData" ) var contentData : String? = null
)
