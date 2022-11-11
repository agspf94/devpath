package com.devpath.dto.trail

import com.devpath.dto.job.JobDTO
import com.devpath.dto.topic.TopicDTO

data class TrailDTO(
    val id: Int,
    val name: String,
    val duration: Int,
    val description: String,
    val averageSalary: String,
    val jobs: MutableSet<JobDTO>,
    val topics: MutableSet<TopicDTO>
)
