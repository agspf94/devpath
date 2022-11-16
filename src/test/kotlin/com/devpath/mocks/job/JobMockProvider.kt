package com.devpath.mocks.job

import com.devpath.dto.job.request.CreateJobRequest
import com.devpath.entity.Job

class JobMockProvider {
    companion object {
        fun getJob(): Job {
            return Job(
                id = 1,
                title = "title",
                location = "location",
                period = "period",
                role = "role",
                link = "link"
            )
        }

        fun getJobRequest(): CreateJobRequest {
            return CreateJobRequest(
                title = "title",
                location = "location",
                period = "period",
                role = "role",
                link = "link"
            )
        }
    }
}
