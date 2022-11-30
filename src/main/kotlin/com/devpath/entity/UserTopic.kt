package com.devpath.entity

import com.devpath.dto.topic.TopicDTO
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne

@Entity
data class UserTopic(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @OneToOne
    val topic: Topic,
    @OneToMany(cascade = [CascadeType.ALL])
    var userSubTopics: MutableSet<UserSubTopic>
) {
    fun toTopicDTO(trail: Trail): TopicDTO {
        val topic = trail.topics.first { it.id == topic.id }
        return TopicDTO(
            id = topic.id!!,
            name = topic.name,
            subTopics = userSubTopics.map { it.toSubTopicDTO(topic) }.toMutableSet()
        )
    }
}
