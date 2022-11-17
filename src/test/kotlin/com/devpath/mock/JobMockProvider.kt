package com.devpath.mock

import com.devpath.dto.job.request.CreateJobRequest
import com.devpath.dto.job.request.UpdateJobRequest
import com.devpath.entity.Job

class JobMockProvider {
    companion object {
        fun getJob(id: Int): Job {
            return Job(
                id = id,
                title = "title",
                location = "location",
                period = "period",
                role = "role",
                link = "link"
            )
        }

        fun getCreateJobRequest(): CreateJobRequest {
            return CreateJobRequest(
                title = "title",
                location = "location",
                period = "period",
                role = "role",
                link = "link"
            )
        }

        fun getUpdateJobRequest(id: Int): UpdateJobRequest {
            return UpdateJobRequest(
                id = id,
                title = "title",
                location = "location",
                period = "period",
                role = "role",
                link = "link"
            )
        }
    }
}
