package com.devpath.dto.trail.request

data class UpdateSubTopicRequest(
    val id: Int,
    var name: String? = null,
    var content: String? = null
)
