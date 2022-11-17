package com.devpath.entity

import com.devpath.dto.job.JobDTO
import com.devpath.dto.topic.TopicDTO
import com.devpath.mock.UserTrailMockProvider.Companion.getUserTrail
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserTrailTests {
    @Test
    fun `User trail should be able to provide an user trail DTO from itself`() {
        val userTrail = getUserTrail(id = 1)
        val trailDTO = userTrail.toTrailDTO()

        assertEquals(userTrail.id, trailDTO.id)
        assertEquals(userTrail.trail.name, trailDTO.name)
        assertEquals(userTrail.trail.duration, trailDTO.duration)
        assertEquals(userTrail.trail.description, trailDTO.description)
        assertEquals(userTrail.trail.averageSalary, trailDTO.averageSalary)
        assertEquals(emptySet<JobDTO>(), trailDTO.jobs)
        assertEquals(emptySet<TopicDTO>(), trailDTO.topics)
    }
}
