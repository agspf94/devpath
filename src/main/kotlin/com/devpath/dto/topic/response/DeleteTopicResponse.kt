package com.devpath.dto.topic.response

import com.devpath.entity.Topic

data class DeleteTopicResponse(
    val topic: Topic,
    val message: String
)
