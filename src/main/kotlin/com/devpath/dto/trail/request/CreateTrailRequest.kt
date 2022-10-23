package com.devpath.dto.trail.request

import com.devpath.entity.Topic
import com.devpath.entity.Trail

data class CreateTrailRequest(
    val name: String,
    val topics: Set<Topic>
) {
    fun toTrail(): Trail {
        return Trail(
            name = name,
            topics = topics
        )
    }
}
