package com.devpath.controller

import com.devpath.mocks.job.JobMockProvider.Companion.getJobRequest
import com.devpath.service.JobService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(JobController::class)
class JobControllerTest {
    @MockBean
    private lateinit var jobService: JobService

    @Autowired
    private lateinit var jobController: JobController

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `Job controller should not be null`() {
        assertNotNull(jobController)
    }

    @Test
    fun `Should create a job successfully`() {
        val createJobRequest = getJobRequest()
        val job = createJobRequest.toJob()

        `when`(jobService.create(createJobRequest)).thenReturn(job)

        mockMvc.perform(post("/job/create")
            .accept(APPLICATION_JSON)
            .content(jacksonObjectMapper().writeValueAsString(createJobRequest))
            .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(job)))

        verify(jobService, times(1)).create(createJobRequest)
    }
}
