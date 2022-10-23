package com.devpath.dto.trail.request

import com.devpath.entity.Trail

data class CreateTrailRequest(
    val name: String
) {
    fun toTrail(): Trail {
        return Trail(
            name = name
        )
    }
}
