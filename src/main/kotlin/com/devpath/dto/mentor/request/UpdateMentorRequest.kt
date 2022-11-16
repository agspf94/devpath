package com.devpath.dto.mentor.request

data class UpdateMentorRequest(
    val userId: Int,
    var role: String? = null,
    var yearsOfExperience: Int? = null,
    var hourCost: Int? = null
)
