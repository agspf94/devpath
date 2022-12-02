package com.devpath.mock

import com.devpath.entity.Schedule
import com.devpath.entity.User

class ScheduleProvider {
    companion object {
        fun getSchedule(id: Int, user: User, date: String): Schedule {
            return Schedule(
                id = id,
                user = user,
                date = date
            )
        }
    }
}
