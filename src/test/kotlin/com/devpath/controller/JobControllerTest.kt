package com.devpath.controller

import com.devpath.mocks.job.JobMockProvider.Companion.getJobRequest
import com.devpath.service.JobService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = RANDOM_PORT)
class JobControllerTest {
    @MockK
    private lateinit var jobService: JobService

    @InjectMockKs
    private lateinit var jobController: JobController

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @LocalServerPort
    private val port = 0

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `Job controller should not be null`() {
        assertNotNull(jobController)
    }

    @Test
    fun `create should create a job successfully`() {
        val createJobRequest = getJobRequest()
        val job = createJobRequest.toJob()

        every { jobService.create(createJobRequest) } returns job

        val result = jobController.create(createJobRequest)
        print(result)

        assertEquals(job, result)
        verify(exactly = 1) { jobService.create(createJobRequest) }
    }
}
