package com.devpath.dto.mentor.request

data class UpdateMentorRequest(
    val userId: Int,
    var description: String? = null,
    var hourCost: Int? = null
)
