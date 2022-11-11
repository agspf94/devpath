package com.devpath.dto.subtopic.response

import com.devpath.entity.SubTopic

data class DeleteSubTopicResponse(
    val subTopic: SubTopic,
    val message: String
)
