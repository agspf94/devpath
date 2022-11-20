package com.devpath.service

import com.devpath.constants.Constants.Companion.JOB_ALREADY_EXISTS
import com.devpath.constants.Constants.Companion.JOB_DELETED
import com.devpath.constants.Constants.Companion.JOB_LIST_IS_EMPTY
import com.devpath.constants.Constants.Companion.JOB_NOT_FOUND
import com.devpath.entity.Job
import com.devpath.exception.exceptions.EmptyJobListException
import com.devpath.exception.exceptions.JobAlreadyExistsException
import com.devpath.mock.JobMockProvider.Companion.getCreateJobRequest
import com.devpath.mock.JobMockProvider.Companion.getJob
import com.devpath.mock.JobMockProvider.Companion.getUpdateJobRequest
import com.devpath.repository.JobRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.any
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.Optional

@SpringBootTest
class JobServiceTests {
    @MockBean
    private lateinit var jobRepository: JobRepository

    @Autowired
    private lateinit var jobService: JobService

    @Test
    fun `Job service should not be null`() {
        assertNotNull(jobService)
    }

    @Test
    fun `Create should return the created job`() {
        val createJobRequest = getCreateJobRequest()
        val job = getJob(id = 1)

        `when`(jobRepository.findByTitle(createJobRequest.title)).thenReturn(Optional.empty())
        `when`(jobRepository.saveAndFlush(createJobRequest.toJob())).thenReturn(job)

        val savedJob = jobService.create(createJobRequest)

        assertAttributes(createJobRequest.toJob(), savedJob)

        verify(jobRepository, times(1)).findByTitle(createJobRequest.title)
        verify(jobRepository, times(1)).saveAndFlush(createJobRequest.toJob())
    }

    @Test
    fun `Create should throw an exception when the job already exists`() {
        val createJobRequest = getCreateJobRequest()
        val job = getJob(id = 1)

        `when`(jobRepository.findByTitle(createJobRequest.title)).thenReturn(Optional.of(job))

        val exception = assertThrows<JobAlreadyExistsException> { jobService.create(createJobRequest) }
        assertEquals(JOB_ALREADY_EXISTS + createJobRequest.title, exception.message)

        verify(jobRepository, times(1)).findByTitle(createJobRequest.title)
        verify(jobRepository, times(0)).saveAndFlush(any())
    }

    @Test
    fun `Read should return the desired job`() {
        val job = getJob(id = 1)

        `when`(jobRepository.findById(job.id!!)).thenReturn(Optional.of(job))

        val savedJob = jobService.read(job.id!!)

        assertAttributes(job, savedJob)

        verify(jobRepository, times(1)).findById(job.id!!)
    }

    @Test
    fun `Read should throw an exception when the job doesn't exist`() {
        val job = getJob(id = 1)

        `when`(jobRepository.findById(job.id!!)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> { jobService.read(job.id!!) }
        assertEquals(JOB_NOT_FOUND + job.id!!, exception.message)

        verify(jobRepository, times(1)).findById(job.id!!)
    }

    @Test
    fun `Read all should return all jobs list`() {
        val job1 = getJob(id = 1)
        val job2 = getJob(id = 2)

        `when`(jobRepository.findAll()).thenReturn(listOf(job1, job2))

        val jobsList = jobService.readAll()

        assertThat(jobsList)
            .hasSize(2)
            .contains(job1, job2)

        verify(jobRepository, times(1)).findAll()
    }

    @Test
    fun `Read all should throw an exception when all jobs list is empty`() {
        `when`(jobRepository.findAll()).thenReturn(listOf())

        val exception = assertThrows<EmptyJobListException> { jobService.readAll() }
        assertEquals(JOB_LIST_IS_EMPTY, exception.message)

        verify(jobRepository, times(1)).findAll()
    }

    @Test
    fun `Update should update the job and return it updated`() {
        val updateJobRequest = getUpdateJobRequest(id = 1)
        val job = getJob(id = 1)

        `when`(jobRepository.findById(job.id!!)).thenReturn(Optional.of(job))
        `when`(jobRepository.saveAndFlush(any())).thenReturn(job)

        val updatedJob = jobService.update(updateJobRequest)

        assertAttributes(job, updatedJob)

        verify(jobRepository, times(1)).findById(job.id!!)
        verify(jobRepository, times(1)).saveAndFlush(any())
    }

    @Test
    fun `Update should throw an exception when the job doesn't exist`() {
        val updateJobRequest = getUpdateJobRequest(id = 1)
        val job = getJob(id = 1)

        `when`(jobRepository.findById(job.id!!)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> { jobService.update(updateJobRequest) }
        assertEquals(JOB_NOT_FOUND + updateJobRequest.id, exception.message)

        verify(jobRepository, times(1)).findById(job.id!!)
        verify(jobRepository, times(0)).saveAndFlush(any())
    }

    @Test
    fun `Delete should delete the desired job successfully`() {
        val job = getJob(id = 1)

        `when`(jobRepository.findById(job.id!!)).thenReturn(Optional.of(job))
        doNothing().`when`(jobRepository).deleteById(job.id!!)

        val deleteJobResponse = jobService.delete(job.id!!)

        assertEquals(job, deleteJobResponse.job)
        assertEquals(JOB_DELETED, deleteJobResponse.message)

        verify(jobRepository, times(1)).findById(job.id!!)
        verify(jobRepository, times(1)).deleteById(job.id!!)
    }

    @Test
    fun `Delete should throw an exception when the job doesn't exist`() {
        val job = getJob(id = 1)

        `when`(jobRepository.findById(job.id!!)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> { jobService.delete(job.id!!) }
        assertEquals(JOB_NOT_FOUND + job.id!!, exception.message)

        verify(jobRepository, times(1)).findById(job.id!!)
        verify(jobRepository, times(0)).deleteById(job.id!!)
    }

    private fun assertAttributes(expectedJob: Job, actualJob: Job) {
        assertNotNull(actualJob.id)
        assertEquals(expectedJob.title, actualJob.title)
        assertEquals(expectedJob.location, actualJob.location)
        assertEquals(expectedJob.period, actualJob.period)
        assertEquals(expectedJob.role, actualJob.role)
        assertEquals(expectedJob.link, actualJob.link)
    }
}
