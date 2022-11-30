package com.devpath.service

import com.devpath.constants.Constants.Companion.TRAIL_ALREADY_EXISTS
import com.devpath.constants.Constants.Companion.TRAIL_DELETED
import com.devpath.constants.Constants.Companion.TRAIL_LIST_IS_EMPTY
import com.devpath.constants.Constants.Companion.TRAIL_NOT_FOUND
import com.devpath.dto.trail.request.CreateTrailRequest
import com.devpath.dto.trail.request.UpdateTrailRequest
import com.devpath.dto.trail.response.DeleteTrailResponse
import com.devpath.entity.Job
import com.devpath.entity.Topic
import com.devpath.entity.Trail
import com.devpath.exception.exceptions.EmptyTrailListException
import com.devpath.exception.exceptions.TrailAlreadyExistsException
import com.devpath.repository.TrailRepository
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class TrailService(
    private val trailRepository: TrailRepository,
    private val jobService: JobService,
    private val topicService: TopicService
) {
    fun create(createTrailRequest: CreateTrailRequest): Trail {
        trailRepository.findByName(createTrailRequest.name)
            .ifPresent { throw TrailAlreadyExistsException(TRAIL_ALREADY_EXISTS + createTrailRequest.name) }
        return trailRepository.saveAndFlush(createTrailRequest.toTrail())
    }

    fun read(id: Int): Trail {
        return trailRepository.findById(id)
            .map { it }
            .orElseThrow { NoSuchElementException(TRAIL_NOT_FOUND + id) }
    }

    fun readAll(): List<Trail> {
        return trailRepository.findAll()
            .stream()
            .collect(Collectors.toList())
            .ifEmpty { throw EmptyTrailListException(TRAIL_LIST_IS_EMPTY) }
    }

    fun update(updateTrailRequest: UpdateTrailRequest): Trail {
        return trailRepository.findById(updateTrailRequest.id)
            .map {
                val updatedTrail = Trail(
                    id = it.id,
                    name = updateTrailRequest.name ?: it.name,
                    duration = updateTrailRequest.duration ?: it.duration,
                    description = updateTrailRequest.description ?: it.description,
                    averageSalary = updateTrailRequest.averageSalary ?: it.averageSalary,
                    jobs = updateJobs(it, updateTrailRequest),
                    topics = updateTopics(it, updateTrailRequest)
                )
                trailRepository.saveAndFlush(updatedTrail)
                updatedTrail
            }
            .orElseThrow { NoSuchElementException(TRAIL_NOT_FOUND + updateTrailRequest.id) }
    }

    fun delete(id: Int): DeleteTrailResponse {
        return trailRepository.findById(id)
            .map {
                trailRepository.deleteById(it.id!!)
                DeleteTrailResponse(it, TRAIL_DELETED)
            }
            .orElseThrow { NoSuchElementException(TRAIL_NOT_FOUND + id) }
    }

    private fun updateJobs(trail: Trail, updateTrailRequest: UpdateTrailRequest): MutableSet<Job> {
        return if (updateTrailRequest.jobsIds == null)
            trail.jobs
        else {
            val jobs = mutableSetOf<Job>()
            updateTrailRequest.jobsIds?.map {
                val job = jobService.read(it)
                jobs.add(job)
            }
            jobs
        }
    }

    private fun updateTopics(trail: Trail, updateTrailRequest: UpdateTrailRequest): MutableSet<Topic> {
        return if (updateTrailRequest.topicsIds == null)
            trail.topics
        else {
            val topics = mutableSetOf<Topic>()
            updateTrailRequest.topicsIds?.map {
                val topic = topicService.read(it)
                topics.add(topic)
            }
            topics
        }
    }
}
