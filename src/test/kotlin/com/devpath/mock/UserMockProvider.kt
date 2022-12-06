package com.devpath.mock

import com.devpath.constants.Constants.Companion.MENTOR_STATUS_ACTIVE
import com.devpath.constants.Constants.Companion.MENTOR_STATUS_INACTIVE
import com.devpath.constants.Constants.Companion.MENTOR_STATUS_PENDING
import com.devpath.dto.user.request.CreateUserRequest
import com.devpath.dto.user.request.UpdateTrailStatusRequest
import com.devpath.dto.user.request.UpdateUserRequest
import com.devpath.entity.User

class UserMockProvider {
    companion object {
        fun getUser(id: Int): User {
            return User(
                id = id,
                name = "name",
                email = "email",
                password = "password",
                mentorStatus = MENTOR_STATUS_INACTIVE,
                userTrails = mutableSetOf()
            )
        }

        fun getMentorPendingUser(id: Int): User {
            return User(
                id = id,
                name = "name",
                email = "email",
                password = "password",
                mentorStatus = MENTOR_STATUS_PENDING,
                userTrails = mutableSetOf()
            )
        }

        fun getMentorUser(id: Int): User {
            return User(
                id = id,
                name = "name",
                email = "email",
                password = "password",
                mentorStatus = MENTOR_STATUS_ACTIVE,
                userTrails = mutableSetOf()
            )
        }

        fun getCreateUserRequest(): CreateUserRequest {
            return CreateUserRequest(
                name = "name",
                email = "email",
                password = "password"
            )
        }

        fun getUpdateUserRequest(id: Int): UpdateUserRequest {
            return UpdateUserRequest(
                id = id,
                name = "name",
                email = "email",
                password = "password",
                mentorStatus = MENTOR_STATUS_INACTIVE
            )
        }

        fun getUpdateTrailStatusRequest(trailId: Int = 1, topicId: Int = 1, subTopicId: Int = 1): UpdateTrailStatusRequest {
            return UpdateTrailStatusRequest(
                userEmail = "userEmail",
                trailId = trailId,
                topicId = topicId,
                subTopicId = subTopicId,
                active = true
            )
        }

        fun getUserWithoutId(): User {
            return User(
                name = "name",
                email = "email",
                password = "password",
                mentorStatus = MENTOR_STATUS_INACTIVE,
                userTrails = mutableSetOf()
            )
        }
    }
}
