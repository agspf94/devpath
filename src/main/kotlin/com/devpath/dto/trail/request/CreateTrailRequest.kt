package com.devpath.dto.trail.request

import com.devpath.entity.Job
import com.devpath.entity.Topic
import com.devpath.entity.Trail

data class CreateTrailRequest(
    val name: String,
    val duration: Int,
    val description: String,
    val averageSalary: String,
    val jobs: MutableSet<Job>,
    val topics: MutableSet<Topic>
) {
    fun toTrail(): Trail {
        return Trail(
            name = name,
            duration = duration,
            description = description,
            averageSalary = averageSalary,
            jobs = jobs,
            topics = topics
        )
    }
}
