package com.devpath.exception.exceptions.mentor

import com.devpath.entity.User

class UserIsNotAMentorException(
    override val message: String?,
    val user: User
) : Exception()
