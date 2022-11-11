package com.devpath.service

import com.devpath.constants.Constants
import com.devpath.constants.Constants.Companion
import com.devpath.constants.Constants.Companion.TRAIL_ALREADY_EXISTS
import com.devpath.constants.Constants.Companion.TRAIL_DELETED
import com.devpath.constants.Constants.Companion.TRAIL_LIST_IS_EMPTY
import com.devpath.constants.Constants.Companion.TRAIL_NOT_FOUND
import com.devpath.dto.trail.request.UpdateTrailRequest
import com.devpath.dto.trail.response.DeleteTrailResponse
import com.devpath.entity.Job
import com.devpath.entity.Trail
import com.devpath.entity.User
import com.devpath.exception.exceptions.EmptyTrailListException
import com.devpath.exception.exceptions.TrailAlreadyExistsException
import com.devpath.repository.TrailRepository
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class TrailService(
    private val trailRepository: TrailRepository
) {
    fun create(trail: Trail): Trail {
        trailRepository.findByName(trail.name)
            .ifPresent { throw TrailAlreadyExistsException(TRAIL_ALREADY_EXISTS + trail.name) }
        return trailRepository.saveAndFlush(trail)
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

//    fun update(updateTrailRequest: UpdateTrailRequest): Trail {
//        return trailRepository.findById(updateTrailRequest.id)
//            .map { trail ->
//                trailRepository.saveAndFlush(
//                    Trail(
//                        id = trail.id,
//                        name = updateTrailRequest.name ?: trail.name,
//                        duration = updateTrailRequest.duration ?: trail.duration,
//                        description = updateTrailRequest.description ?: trail.description,
//                        averageSalary = updateTrailRequest.averageSalary ?: trail.averageSalary,
//                        jobs = updateTrailRequest.jobs?.mapIndexed { i, job ->
//                            Job(
//                                id = job.id,
//                                title = job.title ?: trail.jobs.
//                                location = ,
//                                period = ,
//                                role = ,
//                                link =
//                            )
//                        }.toMutableSet() ?: trail.jobs,
//                        topics = trail.topics
//                    )
//                )
//            }
//            .orElseThrow { NoSuchElementException(TRAIL_NOT_FOUND + updateTrailRequest.id) }
//    }

    fun delete(id: Int): DeleteTrailResponse {
        return trailRepository.findById(id)
            .map {
                trailRepository.deleteById(it.id!!)
                DeleteTrailResponse(it, TRAIL_DELETED)
            }
            .orElseThrow { NoSuchElementException(TRAIL_NOT_FOUND + id) }
    }
}
