package com.devpath.mocks.job

import com.devpath.entity.User

class UserMockProvider {
    companion object {
        fun getUser(id: Int): User {
            return User(
                id = id,
                name = "name",
                email = "email",
                password = "password",
                isMentor = false,
                userTrails = mutableSetOf()
            )
        }
    }
}
