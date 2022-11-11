package com.devpath.dto.topic

import com.devpath.dto.subtopic.SubTopicDTO

data class TopicDTO(
    val id: Int,
    val name: String,
    val subTopics: MutableSet<SubTopicDTO>
)
