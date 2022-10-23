package com.devpath.dto.user.request

data class UpdateTrailStatusRequest(
    val userEmail: String,
    val trailId: Int,
    val topicId: Int,
    val subTopicId: Int,
    val active: Boolean
)
