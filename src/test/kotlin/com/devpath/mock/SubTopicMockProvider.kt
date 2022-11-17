package com.devpath.mock

import com.devpath.dto.subtopic.request.CreateSubTopicRequest
import com.devpath.dto.subtopic.request.UpdateSubTopicRequest
import com.devpath.entity.SubTopic

class SubTopicMockProvider {
    companion object {
        fun getSubTopic(id: Int): SubTopic {
            return SubTopic(
                id = id,
                name = "name",
                content = "content"
            )
        }

        fun getCreateSubTopicRequest(): CreateSubTopicRequest {
            return CreateSubTopicRequest(
                name = "name",
                content = "content"
            )
        }

        fun getUpdateSubTopicRequest(id: Int): UpdateSubTopicRequest {
            return UpdateSubTopicRequest(
                id = id,
                name = "name",
                content = "content"
            )
        }
    }
}
