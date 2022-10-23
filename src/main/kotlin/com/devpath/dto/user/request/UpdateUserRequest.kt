package com.devpath.dto.user.request

data class UpdateUserRequest(
    val id: Int,
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var isMentor: Boolean? = null
)
