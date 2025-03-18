package com.infinityquiz.quizModule.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) representing a solution.
 *
 * This class is used to transfer solution data, typically between a client and a server.
 * It encapsulates the type of content and the actual content data of a solution.
 */
data class SolutionDto (
  @SerializedName("contentType" ) var contentType : String? = null,
  @SerializedName("contentData" ) var contentData : String? = null
)
