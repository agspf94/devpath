package com.devpath.mock

import com.devpath.entity.UserTopic
import com.devpath.mock.TopicMockProvider.Companion.getTopic
import com.devpath.mock.UserSubTopicMockProvider.Companion.getUserSubTopic

class UserTopicMockProvider {
    companion object {
        fun getUserTopic(id: Int): UserTopic {
            return UserTopic(
                id = id,
                topic = getTopic(id = 1),
                userSubTopics = mutableSetOf()
            )
        }

        fun getCompletedUserTopic(id: Int): UserTopic {
            return UserTopic(
                id = id,
                topic = getTopic(id = 1),
                userSubTopics = mutableSetOf(getUserSubTopic(id = 1))
            )
        }
    }
}
