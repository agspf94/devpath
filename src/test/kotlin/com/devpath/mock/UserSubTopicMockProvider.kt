package com.devpath.mock

import com.devpath.entity.UserSubTopic
import com.devpath.mock.SubTopicMockProvider.Companion.getSubTopic

class UserSubTopicMockProvider {
    companion object {
        fun getUserSubTopic(id: Int): UserSubTopic {
            return UserSubTopic(
                id = id,
                subTopic = getSubTopic(id = 1),
                active = false
            )
        }
    }
}
