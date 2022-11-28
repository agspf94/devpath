package com.devpath.entity

import com.devpath.dto.topic.TopicDTO
import javax.persistence.CascadeType.ALL
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OneToOne

@Entity
data class UserTopic(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @OneToOne
    val topic: Topic,
    @OneToMany(cascade = [ALL])
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
