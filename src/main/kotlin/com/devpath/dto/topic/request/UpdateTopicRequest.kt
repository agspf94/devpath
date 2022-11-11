package com.devpath.dto.topic.request

data class UpdateTopicRequest(
    val id: Int,
    var name: String? = null,
    var subTopicsIds: Set<Int>? = null
)
