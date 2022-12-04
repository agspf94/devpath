package com.devpath.dto.user

import com.devpath.dto.trail.TrailDTO
import com.devpath.entity.Schedule

data class UserDTO(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    var mentorStatus: String,
    var trails: MutableSet<TrailDTO>,
    val schedules: MutableSet<Schedule>
)
