package com.devpath.repository

import com.devpath.entity.Job
import com.devpath.mock.JobMockProvider.Companion.getJobWithoutId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class JobRepositoryTests {
    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var jobRepository: JobRepository

    @Test
    fun `Job repository should be able to find a job by title`() {
        val job = getJobWithoutId()
        entityManager.persistAndFlush(job)

        val foundedJob = jobRepository.findByTitle(job.title).get()

        assertAttributes(job, foundedJob)
    }

    @Test
    fun `Job repository should be able to save a new job`() {
        val job = getJobWithoutId()

        jobRepository.saveAndFlush(job)

        val savedJob = jobRepository.findByTitle(job.title).get()

        assertAttributes(job, savedJob)
    }

    @Test
    fun `Job repository should be able to find a job by id`() {
        val job = getJobWithoutId()
        entityManager.persistAndFlush(job)

        val foundedJob = jobRepository.findById(1).get()

        assertAttributes(job, foundedJob)
    }

    @Test
    fun `Job repository should be able to find all jobs`() {
        val job1 = getJobWithoutId()
        val job2 = getJobWithoutId()
        entityManager.persistAndFlush(job1)
        entityManager.persistAndFlush(job2)

        val jobsList = jobRepository.findAll()

        assertThat(jobsList)
            .hasSize(2)
            .contains(job1, job2)
    }

    @Test
    fun `Job repository should be able to find all jobs when the list is empty`() {
        val jobsList = jobRepository.findAll()
        assertThat(jobsList.isEmpty())
    }

    @Test
    fun `Job repository should be able to delete a job by id`() {
        val job = getJobWithoutId()
        entityManager.persistAndFlush(job)

        jobRepository.deleteById(1)

        assertThat(jobRepository.findById(1).isEmpty)
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
