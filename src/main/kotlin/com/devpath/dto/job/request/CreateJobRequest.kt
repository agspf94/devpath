package com.devpath.dto.job.request

import com.devpath.entity.Job

data class CreateJobRequest(
    val title: String,
    val location: String,
    val period: String,
    val role: String,
    val link: String
) {
    fun toJob(): Job {
        return Job(
            title = title,
            location = location,
            period = period,
            role = role,
            link = link
        )
    }
}
