package com.devpath.dto.topic.request

import com.devpath.mock.TopicMockProvider.Companion.getCreateTopicRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CreateTopicRequestTest {
    @Test
    fun `Create topic request should be able to provide a topic from itself`() {
        val createTopicRequest = getCreateTopicRequest()
        val topic = createTopicRequest.toTopic()

        assertEquals(createTopicRequest.name, topic.name)
        assertEquals(createTopicRequest.subTopics, topic.subTopics)
    }
}
