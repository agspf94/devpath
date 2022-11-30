package com.devpath.entity

import com.devpath.dto.subtopic.SubTopicDTO
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

@Entity
data class UserSubTopic(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @OneToOne
    val subTopic: SubTopic,
    var active: Boolean
) {
    fun toSubTopicDTO(topic: Topic): SubTopicDTO {
        val subTopic = topic.subTopics.first { it.id == subTopic.id }
        return SubTopicDTO(
            id = subTopic.id!!,
            name = subTopic.name,
            content = subTopic.content,
            active = active
        )
    }
}
