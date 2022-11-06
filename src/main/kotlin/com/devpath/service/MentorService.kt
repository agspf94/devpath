package com.devpath.service

import com.devpath.constants.Constants.Companion.MENTOR_DEFAULT_DESCRIPTION
import com.devpath.constants.Constants.Companion.MENTOR_DEFAULT_HOUR_COST
import com.devpath.constants.Constants.Companion.MENTOR_DELETED
import com.devpath.constants.Constants.Companion.MENTOR_LIST_IS_EMPTY
import com.devpath.constants.Constants.Companion.USER_IS_NOT_A_MENTOR
import com.devpath.constants.Constants.Companion.USER_NOT_FOUND_ID
import com.devpath.dto.mentor.request.UpdateMentorRequest
import com.devpath.dto.mentor.response.DeleteMentorResponse
import com.devpath.entity.Mentor
import com.devpath.exception.exceptions.EmptyMentorListException
import com.devpath.exception.exceptions.UserIsNotAMentorException
import com.devpath.repository.MentorRepository
import com.devpath.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class MentorService(
    private val mentorRepository: MentorRepository,
    private val userRepository: UserRepository
) {
    fun read(userId: Int): Mentor {
        return userRepository.findById(userId)
            .map {
                if (it.isMentor) {
                    mentorRepository.findById(it.id!!).get()
                } else {
                    throw UserIsNotAMentorException(USER_IS_NOT_A_MENTOR, it)
                }
            }
            .orElseThrow { NoSuchElementException(USER_NOT_FOUND_ID + userId) }
    }

    fun readAll(): List<Mentor> {
        return mentorRepository.findAll()
            .stream()
            .collect(Collectors.toList())
            .ifEmpty { throw EmptyMentorListException(MENTOR_LIST_IS_EMPTY) }
    }

    fun becomeMentor(userId: Int): Mentor {
        return userRepository.findById(userId)
            .map {
                it.isMentor = true
                userRepository.saveAndFlush(it)
                mentorRepository.saveAndFlush(
                    Mentor(
                        id = it.id,
                        user = it,
                        description = MENTOR_DEFAULT_DESCRIPTION,
                        hourCost = MENTOR_DEFAULT_HOUR_COST,
                        payments = mutableSetOf(),
                        schedules = mutableSetOf()
                    )
                )
            }
            .orElseThrow { NoSuchElementException(USER_NOT_FOUND_ID + userId) }
    }

    fun update(updateMentorRequest: UpdateMentorRequest): Mentor {
        return userRepository.findById(updateMentorRequest.userId)
            .map {
                if (it.isMentor) {
                    val mentor = mentorRepository.findById(it.id!!).get()
                    mentorRepository.saveAndFlush(
                        Mentor(
                            id = it.id,
                            user = it,
                            description = updateMentorRequest.description ?: mentor.description,
                            hourCost = updateMentorRequest.hourCost ?: mentor.hourCost,
                            payments = mentor.payments,
                            schedules = mentor.schedules
                        )
                    )
                } else {
                    throw UserIsNotAMentorException(USER_IS_NOT_A_MENTOR, it)
                }
            }
            .orElseThrow { NoSuchElementException(USER_NOT_FOUND_ID + updateMentorRequest.userId) }
    }

    fun delete(userId: Int): DeleteMentorResponse {
        return userRepository.findById(userId)
            .map {
                it.isMentor = false
                userRepository.saveAndFlush(it)
                DeleteMentorResponse(it, MENTOR_DELETED)
            }
            .orElseThrow { NoSuchElementException(USER_NOT_FOUND_ID + userId) }
    }
}
