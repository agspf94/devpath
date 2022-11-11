package com.devpath.dto.user.response

import com.devpath.entity.User

data class DeleteUserTrailResponse(
    val user: User,
    val message: String
)
