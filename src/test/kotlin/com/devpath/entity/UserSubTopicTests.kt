package com.devpath.entity

import com.devpath.mock.SubTopicMockProvider.Companion.getSubTopic
import com.devpath.mock.TopicMockProvider.Companion.getTopic
import com.devpath.mock.UserSubTopicMockProvider.Companion.getUserSubTopic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserSubTopicTests {
    @Test
    fun `User sub topic should be able to provide an user sub topic DTO from itself`() {
        val topic = getTopic(id = 1)
        val subTopic = getSubTopic(id = 1)
        topic.subTopics.add(subTopic)
        val userSubTopic = getUserSubTopic(id = 1)
        val subTopicDTO = userSubTopic.toSubTopicDTO(topic)

        assertEquals(userSubTopic.id, subTopicDTO.id)
        assertEquals(userSubTopic.subTopic.name, subTopicDTO.name)
        assertEquals(userSubTopic.subTopic.content, subTopicDTO.content)
        assertEquals(userSubTopic.active, subTopicDTO.active)
    }
}
