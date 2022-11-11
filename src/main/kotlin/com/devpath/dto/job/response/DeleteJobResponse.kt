package com.devpath.dto.job.response

import com.devpath.entity.Job

data class DeleteJobResponse(
    val job: Job,
    val message: String
)
