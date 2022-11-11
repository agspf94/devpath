package com.devpath.dto.trail.request

data class UpdateTopicRequest(
    val id: Int,
    var name: String? = null,
    var subTopics: Set<UpdateSubTopicRequest>? = null
)
