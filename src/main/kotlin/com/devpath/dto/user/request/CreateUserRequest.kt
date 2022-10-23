package com.devpath.dto.user.request

import com.devpath.entity.User

data class CreateUserRequest(
    var name: String,
    var email: String,
    var password: String,
    var isMentor: Boolean
) {
    fun toUser(): User {
        return User(
            name = name,
            email = email,
            password = password,
            isMentor = isMentor
        )
    }
}
