package com.devpath.entity

import com.devpath.mock.JobMockProvider.Companion.getJob
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JobTests {
    @Test
    fun `Job should be able to provide a job DTO from itself`() {
        val job = getJob(id = 1)
        val jobDTO = job.toJobDTO()

        assertEquals(job.id, jobDTO.id)
        assertEquals(job.title, jobDTO.title)
        assertEquals(job.location, jobDTO.location)
        assertEquals(job.period, jobDTO.period)
        assertEquals(job.role, jobDTO.role)
        assertEquals(job.link, jobDTO.link)
    }
}
