package com.devpath.service

import com.devpath.constants.Constants.Companion.TRAIL_ALREADY_EXISTS
import com.devpath.constants.Constants.Companion.TRAIL_DELETED
import com.devpath.constants.Constants.Companion.TRAIL_LIST_IS_EMPTY
import com.devpath.constants.Constants.Companion.TRAIL_NOT_FOUND
import com.devpath.dto.trail.request.CreateTrailRequest
import com.devpath.dto.trail.response.DeleteTrailResponse
import com.devpath.entity.Trail
import com.devpath.exception.exceptions.EmptyTrailListException
import com.devpath.exception.exceptions.TrailAlreadyExistsException
import com.devpath.repository.TrailRepository
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class TrailService(
    private val trailRepository: TrailRepository
) {
    fun createTrail(createTrailRequest: CreateTrailRequest): Trail {
        trailRepository.findByName(createTrailRequest.name)
            .ifPresent { throw TrailAlreadyExistsException(TRAIL_ALREADY_EXISTS + createTrailRequest.name) }
        return trailRepository.saveAndFlush(createTrailRequest.toTrail())
    }

    fun readTrail(id: Int): Trail {
        return trailRepository.findById(id)
            .map { it }
            .orElseThrow { NoSuchElementException(TRAIL_NOT_FOUND + id) }
    }

    fun readAllTrails(): List<Trail> {
        return trailRepository.findAll()
            .stream()
            .collect(Collectors.toList())
            .ifEmpty { throw EmptyTrailListException(TRAIL_LIST_IS_EMPTY) }
    }

    fun deleteTrail(id: Int): DeleteTrailResponse {
        return trailRepository.findById(id)
            .map {
                trailRepository.deleteById(it.id!!)
                DeleteTrailResponse(it, TRAIL_DELETED)
            }
            .orElseThrow { NoSuchElementException(TRAIL_NOT_FOUND + id) }
    }
}
