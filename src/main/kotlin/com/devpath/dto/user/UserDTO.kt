package com.devpath.dto.user

import com.devpath.dto.trail.TrailDTO

data class UserDTO(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    var isMentor: Boolean,
    var trails: MutableSet<TrailDTO>
)
