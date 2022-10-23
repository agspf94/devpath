package com.devpath.dto.trail.response

import com.devpath.entity.Trail

data class DeleteTrailResponse(
    val trail: Trail,
    val message: String
)
