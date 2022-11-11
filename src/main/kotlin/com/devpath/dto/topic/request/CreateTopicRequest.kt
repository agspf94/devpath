package com.devpath.dto.topic.request

import com.devpath.entity.SubTopic
import com.devpath.entity.Topic

data class CreateTopicRequest(
    val name: String,
    val subTopics: MutableSet<SubTopic>
) {
    fun toTopic(): Topic {
        return Topic(
            name = name,
            subTopics = subTopics
        )
    }
}
