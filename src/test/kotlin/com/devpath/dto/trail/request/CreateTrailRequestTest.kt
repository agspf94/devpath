package com.devpath.dto.trail.request

import com.devpath.mock.TrailMockProvider.Companion.getCreateTrailRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CreateTrailRequestTest {
    @Test
    fun `Create trail request should be able to provide a trail from itself`() {
        val createTrailRequest = getCreateTrailRequest()
        val trail = createTrailRequest.toTrail()

        assertEquals(createTrailRequest.name, trail.name)
        assertEquals(createTrailRequest.duration, trail.duration)
        assertEquals(createTrailRequest.description, trail.description)
        assertEquals(createTrailRequest.averageSalary, trail.averageSalary)
        assertEquals(createTrailRequest.jobs, trail.jobs)
        assertEquals(createTrailRequest.topics, trail.topics)
    }
}
