package com.devpath.mock

import com.devpath.dto.topic.request.CreateTopicRequest
import com.devpath.dto.topic.request.UpdateTopicRequest
import com.devpath.entity.Topic

class TopicMockProvider {
    companion object {
        fun getTopic(id: Int): Topic {
            return Topic(
                id = id,
                name = "name",
                subTopics = mutableSetOf()
            )
        }

        fun getCreateTopicRequest(): CreateTopicRequest {
            return CreateTopicRequest(
                name = "name",
                subTopics = mutableSetOf()
            )
        }

        fun getUpdateTopicRequest(id: Int): UpdateTopicRequest {
            return UpdateTopicRequest(
                id = id,
                name = "name",
                subTopicsIds = mutableSetOf()
            )
        }

        fun getTopicWithoutId(): Topic {
            return Topic(
                name = "name",
                subTopics = mutableSetOf()
            )
        }
    }
}
