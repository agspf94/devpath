package com.devpath.dto.subtopic.request

import com.devpath.mock.SubTopicMockProvider.Companion.getCreateSubTopicRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CreateSubTopicRequestTest {
    @Test
    fun `Create sub topic request should be able to provide a sub topic from itself`() {
        val createSubTopicRequest = getCreateSubTopicRequest()
        val subTopic = createSubTopicRequest.toSubTopic()

        assertEquals(createSubTopicRequest.name, subTopic.name)
        assertEquals(createSubTopicRequest.content, subTopic.content)
    }
}
