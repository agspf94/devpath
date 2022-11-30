package com.devpath.service

import com.devpath.constants.Constants.Companion.JOB_ALREADY_EXISTS
import com.devpath.constants.Constants.Companion.JOB_DELETED
import com.devpath.constants.Constants.Companion.JOB_LIST_IS_EMPTY
import com.devpath.constants.Constants.Companion.JOB_NOT_FOUND
import com.devpath.dto.job.request.CreateJobRequest
import com.devpath.dto.job.request.UpdateJobRequest
import com.devpath.dto.job.response.DeleteJobResponse
import com.devpath.entity.Job
import com.devpath.exception.exceptions.EmptyJobListException
import com.devpath.exception.exceptions.JobAlreadyExistsException
import com.devpath.repository.JobRepository
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class JobService(
    private val jobRepository: JobRepository
) {
    fun create(createJobRequest: CreateJobRequest): Job {
        jobRepository.findByTitle(createJobRequest.title)
            .ifPresent { throw JobAlreadyExistsException(JOB_ALREADY_EXISTS + createJobRequest.title) }
        return jobRepository.saveAndFlush(createJobRequest.toJob())
    }

    fun read(id: Int): Job {
        return jobRepository.findById(id)
            .map { it }
            .orElseThrow { NoSuchElementException(JOB_NOT_FOUND + id) }
    }

    fun readAll(): List<Job> {
        return jobRepository.findAll()
            .stream()
            .collect(Collectors.toList())
            .ifEmpty { throw EmptyJobListException(JOB_LIST_IS_EMPTY) }
    }

    fun update(updateJobRequest: UpdateJobRequest): Job {
        return jobRepository.findById(updateJobRequest.id)
            .map {
                jobRepository.saveAndFlush(
                    Job(
                        id = it.id,
                        title = updateJobRequest.title ?: it.title,
                        location = updateJobRequest.location ?: it.location,
                        period = updateJobRequest.period ?: it.period,
                        role = updateJobRequest.role ?: it.role,
                        link = updateJobRequest.link ?: it.link
                    )
                )
            }
            .orElseThrow { NoSuchElementException(JOB_NOT_FOUND + updateJobRequest.id) }
    }

    fun delete(id: Int): DeleteJobResponse {
        return jobRepository.findById(id)
            .map {
                jobRepository.deleteById(it.id!!)
                DeleteJobResponse(it, JOB_DELETED)
            }
            .orElseThrow { NoSuchElementException(JOB_NOT_FOUND + id) }
    }
}
