package com.devpath.entity

import com.devpath.dto.topic.TopicDTO
import javax.persistence.CascadeType.ALL
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class UserTopic(
    @Id
    @GeneratedValue(generator = "user_topic_generator")
    val id: Int? = null,
    val topicId: Int,
    @OneToMany(cascade = [ALL])
    var userSubTopics: MutableSet<UserSubTopic>
) {
    fun toTopicDTO(trail: Trail): TopicDTO {
        val topic = trail.topics.first { it.id == topicId }
        return TopicDTO(
            id = id!!,
            name = topic.name,
            subTopics = userSubTopics.map { it.toSubTopicDTO(topic) }.toMutableSet()
        )
    }
}
