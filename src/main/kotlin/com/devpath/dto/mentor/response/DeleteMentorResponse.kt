package com.devpath.dto.mentor.response

import com.devpath.entity.User

data class DeleteMentorResponse(
    val user: User,
    val message: String
)
