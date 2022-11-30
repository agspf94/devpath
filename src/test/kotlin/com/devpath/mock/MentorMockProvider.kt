package com.devpath.mock

import com.devpath.constants.Constants.Companion.MENTOR_DEFAULT_HOUR_COST
import com.devpath.constants.Constants.Companion.MENTOR_DEFAULT_ROLE
import com.devpath.constants.Constants.Companion.MENTOR_DEFAULT_YEARS_OF_EXPERIENCE
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
                role = MENTOR_DEFAULT_ROLE,
                yearsOfExperience = MENTOR_DEFAULT_YEARS_OF_EXPERIENCE,
                hourCost = MENTOR_DEFAULT_HOUR_COST,
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
