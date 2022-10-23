package com.devpath.dto.user.response

import com.devpath.entity.User

data class DeleteUserResponse(
    val user: User,
    val message: String
)
