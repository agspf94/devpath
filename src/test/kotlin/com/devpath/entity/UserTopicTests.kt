package com.devpath.entity

import com.devpath.dto.subtopic.SubTopicDTO
import com.devpath.mock.TopicMockProvider.Companion.getTopic
import com.devpath.mock.TrailMockProvider.Companion.getTrail
import com.devpath.mock.UserTopicMockProvider.Companion.getUserTopic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserTopicTests {
    @Test
    fun `User topic should be able to provide an user topic DTO from itself`() {
        val trail = getTrail(id = 1)
        val topic = getTopic(id = 1)
        trail.topics.add(topic)
        val userTopic = getUserTopic(id = 1)
        val topicDTO = userTopic.toTopicDTO(trail)

        assertEquals(userTopic.id, topicDTO.id)
        assertEquals(userTopic.topic.name, topicDTO.name)
        assertEquals(emptySet<SubTopicDTO>(), topicDTO.subTopics)
    }
}
