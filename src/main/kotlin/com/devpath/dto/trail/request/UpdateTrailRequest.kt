package com.devpath.dto.trail.request

data class UpdateTrailRequest(
    val id: Int,
    var name: String? = null,
    var duration: Int? = null,
    var description: String? = null,
    var averageSalary: String? = null,
    var jobsIds: Set<Int>? = null,
    var topicsIds: Set<Int>? = null
)
