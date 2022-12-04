package com.devpath.controller

import com.devpath.entity.Mentor
import com.devpath.service.MentorService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class AdminController(
    private val mentorService: MentorService
) {
    @PostMapping("/approve-mentor/{userId}")
    fun approveMentor(@PathVariable userId: Int): Mentor {
        return mentorService.approveMentor(userId)
    }

    @PostMapping("/approve-schedule/{mentorId}/{scheduleId}")
    fun approveSchedule(
        @PathVariable mentorId: Int,
        @PathVariable scheduleId: Int
    ): Mentor {
        return mentorService.approveSchedule(mentorId, scheduleId)
    }
}
