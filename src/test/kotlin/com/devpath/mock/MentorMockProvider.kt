package com.devpath.mock

import com.devpath.constants.Constants.Companion.MENTOR_DEFAULT_HOUR_COST
import com.devpath.constants.Constants.Companion.MENTOR_DEFAULT_ROLE
import com.devpath.constants.Constants.Companion.MENTOR_DEFAULT_YEARS_OF_EXPERIENCE
import com.devpath.constants.Constants.Companion.MENTOR_STATUS_ACTIVE
import com.devpath.constants.Constants.Companion.MENTOR_STATUS_PENDING
import com.devpath.dto.mentor.request.UpdateMentorRequest
import com.devpath.entity.Mentor
import com.devpath.entity.User

class MentorMockProvider {
    companion object {
        fun getPendingMentor(id: Int, user: User): Mentor {
            user.mentorStatus = MENTOR_STATUS_PENDING
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

        fun getApprovedMentor(id: Int, user: User): Mentor {
            user.mentorStatus = MENTOR_STATUS_ACTIVE
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
