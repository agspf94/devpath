package com.devpath.entity

import com.devpath.dto.subtopic.SubTopicDTO
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToOne

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
            id = id!!,
            name = subTopic.name,
            content = subTopic.content,
            active = active
        )
    }
}
