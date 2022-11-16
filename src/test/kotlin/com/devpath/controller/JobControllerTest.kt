package com.devpath.controller

import com.devpath.constants.Constants.Companion.JOB_ALREADY_EXISTS
import com.devpath.constants.Constants.Companion.JOB_LIST_IS_EMPTY
import com.devpath.constants.Constants.Companion.JOB_NOT_FOUND
import com.devpath.dto.job.response.DeleteJobResponse
import com.devpath.exception.ErrorMessage
import com.devpath.exception.exceptions.EmptyJobListException
import com.devpath.exception.exceptions.JobAlreadyExistsException
import com.devpath.mocks.JobMockProvider.Companion.getJob
import com.devpath.mocks.JobMockProvider.Companion.getCreateJobRequest
import com.devpath.mocks.JobMockProvider.Companion.getUpdateJobRequest
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
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
        val createJobRequest = getCreateJobRequest()
        val job = getJob(id = 1)

        `when`(jobService.create(createJobRequest)).thenReturn(job)

        mockMvc.perform(
                post("/job/create")
                    .accept(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(createJobRequest))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(job)))

        verify(jobService, times(1)).create(createJobRequest)
    }

    @Test
    fun `Should fails while creating a job with a title that already exists`() {
        val createJobRequest = getCreateJobRequest()
        val errorMessage = JOB_ALREADY_EXISTS + createJobRequest.title

        `when`(jobService.create(createJobRequest)).thenAnswer { throw JobAlreadyExistsException(errorMessage) }

        mockMvc.perform(
                post("/job/create")
                    .accept(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(createJobRequest))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(jobService, times(1)).create(createJobRequest)
    }

    @Test
    fun `Should read a job successfully`() {
        val job = getJob(id = 1)

        `when`(jobService.read(job.id!!)).thenReturn(job)

        mockMvc.perform(
                get("/job/${job.id!!}")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(job)))

        verify(jobService, times(1)).read(job.id!!)
    }

    @Test
    fun `Should fails while reading a job that doesn't exist`() {
        val jobId = 1
        val errorMessage = JOB_ALREADY_EXISTS + jobId

        `when`(jobService.read(jobId)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
                get("/job/$jobId")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(jobService, times(1)).read(jobId)
    }

    @Test
    fun `Should read all jobs list successfully`() {
        val jobsList = listOf(getJob(id = 1), getJob(id = 2))

        `when`(jobService.readAll()).thenReturn(jobsList)

        mockMvc.perform(
                get("/job/all")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(jobsList)))

        verify(jobService, times(1)).readAll()
    }

    @Test
    fun `Should fails while reading all jobs list because there aren't any`() {
        val errorMessage = JOB_LIST_IS_EMPTY

        `when`(jobService.readAll()).thenAnswer { throw EmptyJobListException(errorMessage) }

        mockMvc.perform(
                get("/job/all")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(jobService, times(1)).readAll()
    }

    @Test
    fun `Should update a job successfully`() {
        val updateJobRequest = getUpdateJobRequest(id = 1)
        val job = getJob(updateJobRequest.id)

        `when`(jobService.update(updateJobRequest)).thenReturn(job)

        mockMvc.perform(
                patch("/job/update")
                    .accept(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(updateJobRequest))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(job)))

        verify(jobService, times(1)).update(updateJobRequest)
    }

    @Test
    fun `Should fails while updating a job that doesn't exist`() {
        val updateJobRequest = getUpdateJobRequest(id = 1)
        val errorMessage = JOB_NOT_FOUND + updateJobRequest.id

        `when`(jobService.update(updateJobRequest)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
                patch("/job/update")
                    .accept(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(updateJobRequest))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(jobService, times(1)).update(updateJobRequest)
    }

    @Test
    fun `Should delete a job successfully`() {
        val job = getJob(id = 1)
        val deleteJobResponse = DeleteJobResponse(job, JOB_NOT_FOUND + job.id)

        `when`(jobService.delete(job.id!!)).thenReturn(deleteJobResponse)

        mockMvc.perform(
                delete("/job/delete/${job.id!!}")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(deleteJobResponse)))

        verify(jobService, times(1)).delete(job.id!!)
    }

    @Test
    fun `Should fails while deleting a job that doesn't exist`() {
        val jobId = 1
        val errorMessage = JOB_NOT_FOUND + jobId

        `when`(jobService.delete(jobId)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
                delete("/job/delete/$jobId")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(jobService, times(1)).delete(jobId)
    }
}
