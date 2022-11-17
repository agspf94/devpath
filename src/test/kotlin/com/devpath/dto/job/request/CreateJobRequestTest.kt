package com.devpath.dto.job.request

import com.devpath.mock.JobMockProvider.Companion.getCreateJobRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CreateJobRequestTest {
    @Test
    fun `Create job request should be able to provide a job from itself`() {
        val createJobRequest = getCreateJobRequest()
        val job = createJobRequest.toJob()

        assertEquals(createJobRequest.title, job.title)
        assertEquals(createJobRequest.location, job.location)
        assertEquals(createJobRequest.period, job.period)
        assertEquals(createJobRequest.role, job.role)
        assertEquals(createJobRequest.link, job.link)
    }
}
