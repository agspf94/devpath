package com.devpath.entity

import com.devpath.dto.subtopic.SubTopicDTO
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class UserSubTopic(
    @Id
    @GeneratedValue(generator = "user_sub_topic_generator")
    val id: Int? = null,
    val subTopicId: Int,
    var active: Boolean
) {
    fun toSubTopicDTO(topic: Topic): SubTopicDTO {
        val subTopic = topic.subTopics.first { it.id == subTopicId }
        return SubTopicDTO(
            id = id!!,
            name = subTopic.name,
            content = subTopic.content,
            active = active
        )
    }
}
