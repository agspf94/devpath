package com.devpath.mock

import com.devpath.dto.trail.request.CreateTrailRequest
import com.devpath.dto.trail.request.UpdateTrailRequest
import com.devpath.entity.Trail

class TrailMockProvider {
    companion object {
        fun getTrail(id: Int): Trail {
            return Trail(
                id = id,
                name = "name",
                duration = 0,
                description = "description",
                averageSalary = "averageSalary",
                jobs = mutableSetOf(),
                topics = mutableSetOf()
            )
        }

        fun getCreateTrailRequest(): CreateTrailRequest {
            return CreateTrailRequest(
                name = "name",
                duration = 0,
                description = "description",
                averageSalary = "averageSalary",
                jobs = mutableSetOf(),
                topics = mutableSetOf()
            )
        }

        fun getUpdateTrailRequest(id: Int): UpdateTrailRequest {
            return UpdateTrailRequest(
                id = id,
                name = "name",
                duration = 0,
                description = "description",
                averageSalary = "averageSalary",
                jobsIds = mutableSetOf(),
                topicsIds = mutableSetOf()
            )
        }

        fun getTrailWithoutId(): Trail {
            return Trail(
                name = "name",
                duration = 0,
                description = "description",
                averageSalary = "averageSalary",
                jobs = mutableSetOf(),
                topics = mutableSetOf()
            )
        }
    }
}
