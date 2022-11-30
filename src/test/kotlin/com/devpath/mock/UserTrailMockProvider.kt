package com.devpath.mock

import com.devpath.entity.UserTrail
import com.devpath.mock.TrailMockProvider.Companion.getTrail
import com.devpath.mock.UserTopicMockProvider.Companion.getCompletedUserTopic

class UserTrailMockProvider {
    companion object {
        fun getUserTrail(id: Int): UserTrail {
            return UserTrail(
                id = id,
                trail = getTrail(id = 1),
                userTopics = mutableSetOf()
            )
        }

        fun getUserTrailWithoutId(): UserTrail {
            return UserTrail(
                trail = getTrail(id = 1),
                userTopics = mutableSetOf()
            )
        }

        fun getCompletedUserTrail(id: Int): UserTrail {
            return UserTrail(
                id = id,
                trail = getTrail(id = 1),
                userTopics = mutableSetOf(getCompletedUserTopic(id = 1))
            )
        }
    }
}
