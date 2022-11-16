package com.devpath.mocks.job

import com.devpath.dto.mentor.request.UpdateMentorRequest
import com.devpath.entity.Mentor
import com.devpath.entity.User

class MentorMockProvider {
    companion object {
        fun getMentor(id: Int, user: User): Mentor {
            user.isMentor = true
            return Mentor(
                id = id,
                user = user,
                role = "role",
                yearsOfExperience = 0,
                hourCost = 0,
                payments = mutableSetOf(),
                schedules = mutableSetOf()
            )
        }

        fun getUpdateMentorRequest(userId: Int): UpdateMentorRequest {
            return UpdateMentorRequest(
                userId = userId,
                role = "role",
                yearsOfExperience = 0,
                hourCost = 0
            )
        }
    }
}
