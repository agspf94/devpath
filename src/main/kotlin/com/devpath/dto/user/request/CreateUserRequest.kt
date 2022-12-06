package com.devpath.dto.user.request

import com.devpath.constants.Constants.Companion.MENTOR_STATUS_INACTIVE
import com.devpath.entity.User

data class CreateUserRequest(
    val name: String,
    val email: String,
    val password: String
) {
    fun toUser(): User {
        return User(
            name = name,
            email = email,
            password = password,
            mentorStatus = MENTOR_STATUS_INACTIVE,
            userTrails = mutableSetOf()
        )
    }
}
