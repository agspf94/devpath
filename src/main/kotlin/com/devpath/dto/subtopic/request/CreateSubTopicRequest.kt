package com.devpath.dto.subtopic.request

import com.devpath.entity.SubTopic

data class CreateSubTopicRequest(
    val name: String,
    val content: String
) {
    fun toSubTopic(): SubTopic {
        return SubTopic(
            name = name,
            content = content
        )
    }
}
