package com.devpath.dto.user.request

import com.devpath.entity.User

data class CreateUserRequest(
    val name: String,
    val email: String,
    val password: String,
    val mentorStatus: String
) {
    fun toUser(): User {
        return User(
            name = name,
            email = email,
            password = password,
            mentorStatus = mentorStatus,
            userTrails = mutableSetOf()
        )
    }
}
