package com.devpath.service

import com.devpath.constants.Constants.Companion.CAN_ONLY_CANCEL_SCHEDULE_AVAILABLE
import com.devpath.constants.Constants.Companion.MENTOR_AND_USER_ARE_THE_SAME
import com.devpath.constants.Constants.Companion.MENTOR_DEFAULT_HOUR_COST
import com.devpath.constants.Constants.Companion.MENTOR_DEFAULT_ROLE
import com.devpath.constants.Constants.Companion.MENTOR_DEFAULT_YEARS_OF_EXPERIENCE
import com.devpath.constants.Constants.Companion.MENTOR_DELETED
import com.devpath.constants.Constants.Companion.MENTOR_LIST_IS_EMPTY
import com.devpath.constants.Constants.Companion.MENTOR_STATUS_ACTIVE
import com.devpath.constants.Constants.Companion.MENTOR_STATUS_INACTIVE
import com.devpath.constants.Constants.Companion.MENTOR_STATUS_PENDING
import com.devpath.constants.Constants.Companion.SCHEDULE_AVAILABLE
import com.devpath.constants.Constants.Companion.SCHEDULE_CANCELLED
import com.devpath.constants.Constants.Companion.SCHEDULE_NOT_AVAILABLE
import com.devpath.constants.Constants.Companion.SCHEDULE_NOT_FOUND
import com.devpath.constants.Constants.Companion.SCHEDULE_NOT_PENDING
import com.devpath.constants.Constants.Companion.SCHEDULE_PENDING
import com.devpath.constants.Constants.Companion.SCHEDULE_RESERVED
import com.devpath.constants.Constants.Companion.USER_DIDNT_REQUEST_TO_BECOME_A_MENTOR
import com.devpath.constants.Constants.Companion.USER_IS_NOT_A_MENTOR
import com.devpath.constants.Constants.Companion.USER_NOT_FOUND_ID
import com.devpath.dto.mentor.request.UpdateMentorRequest
import com.devpath.dto.mentor.response.DeleteMentorResponse
import com.devpath.entity.Mentor
import com.devpath.entity.Schedule
import com.devpath.entity.User
import com.devpath.exception.exceptions.mentor.EmptyMentorListException
import com.devpath.exception.exceptions.mentor.MentorAndUserAreTheSameException
import com.devpath.exception.exceptions.mentor.UserDidntRequestToBecomeAMentorException
import com.devpath.exception.exceptions.mentor.UserIsNotAMentorException
import com.devpath.exception.exceptions.schedule.CanOnlyCancelScheduleAvailableException
import com.devpath.exception.exceptions.schedule.ScheduleNotAvailableException
import com.devpath.exception.exceptions.schedule.ScheduleNotFoundException
import com.devpath.exception.exceptions.schedule.ScheduleNotPendingException
import com.devpath.repository.MentorRepository
import com.devpath.repository.ScheduleRepository
import com.devpath.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class MentorService(
    private val mentorRepository: MentorRepository,
    private val userRepository: UserRepository,
    private val scheduleRepository: ScheduleRepository
) {
    fun becomeMentor(userId: Int): User {
        return userRepository.findById(userId)
            .map {
                it.mentorStatus = MENTOR_STATUS_PENDING
                userRepository.saveAndFlush(it)
            }
            .orElseThrow { NoSuchElementException(USER_NOT_FOUND_ID + userId) }
    }

    fun read(userId: Int): Mentor {
        return userRepository.findById(userId)
            .map {
                if (it.mentorStatus == MENTOR_STATUS_ACTIVE) {
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

    fun update(updateMentorRequest: UpdateMentorRequest): Mentor {
        return userRepository.findById(updateMentorRequest.userId)
            .map {
                if (it.mentorStatus == MENTOR_STATUS_ACTIVE) {
                    val mentor = mentorRepository.findById(it.id!!).get()
                    val updatedMentor = Mentor(
                        id = it.id,
                        user = it,
                        role = updateMentorRequest.role ?: mentor.role,
                        yearsOfExperience = updateMentorRequest.yearsOfExperience ?: mentor.yearsOfExperience,
                        hourCost = updateMentorRequest.hourCost ?: mentor.hourCost,
                        payments = mentor.payments,
                        schedules = mentor.schedules
                    )
                    mentorRepository.saveAndFlush(updatedMentor)
                    updatedMentor
                } else {
                    throw UserIsNotAMentorException(USER_IS_NOT_A_MENTOR, it)
                }
            }
            .orElseThrow { NoSuchElementException(USER_NOT_FOUND_ID + updateMentorRequest.userId) }
    }

    fun delete(userId: Int): DeleteMentorResponse {
        return userRepository.findById(userId)
            .map {
                if (it.mentorStatus == MENTOR_STATUS_ACTIVE) {
                    val mentor = mentorRepository.findByUser(it).get()
                    mentorRepository.deleteById(mentor.id!!)
                    it.mentorStatus = MENTOR_STATUS_INACTIVE
                    userRepository.saveAndFlush(it)
                    DeleteMentorResponse(it, MENTOR_DELETED)
                } else {
                    throw UserIsNotAMentorException(USER_IS_NOT_A_MENTOR, it)
                }
            }
            .orElseThrow { NoSuchElementException(USER_NOT_FOUND_ID + userId) }
    }

    fun approveMentor(userId: Int): Mentor {
        return userRepository.findById(userId)
            .map {
                if (it.mentorStatus == MENTOR_STATUS_PENDING) {
                    it.mentorStatus = MENTOR_STATUS_ACTIVE
                    userRepository.saveAndFlush(it)
                    val approvedMentor = Mentor(
                        id = it.id,
                        user = it,
                        role = MENTOR_DEFAULT_ROLE,
                        yearsOfExperience = MENTOR_DEFAULT_YEARS_OF_EXPERIENCE,
                        hourCost = MENTOR_DEFAULT_HOUR_COST,
                        payments = mutableSetOf(),
                        schedules = mutableSetOf()
                    )
                    mentorRepository.saveAndFlush(approvedMentor)
                    approvedMentor
                } else {
                    throw UserDidntRequestToBecomeAMentorException(USER_DIDNT_REQUEST_TO_BECOME_A_MENTOR)
                }
            }
            .orElseThrow { NoSuchElementException(USER_NOT_FOUND_ID + userId) }
    }

    fun createSchedule(mentorId: Int, date: String): Mentor {
        val mentor = read(mentorId)
        val schedule = Schedule(
            mentorEmail = mentor.user.email,
            date = date,
            status = SCHEDULE_AVAILABLE
        )
        scheduleRepository.saveAndFlush(schedule)
        mentor.schedules.add(schedule)
        mentorRepository.saveAndFlush(mentor)
        return mentor
    }

    fun reserveSchedule(mentorId: Int, scheduleId: Int, userId: Int): Mentor {
        val mentor = read(mentorId)
        val user = userRepository.findById(userId)
            .orElseThrow { NoSuchElementException(USER_NOT_FOUND_ID + userId) }
        validateUserEqualsMentor(mentor, user)
        val schedule = mentor.schedules.firstOrNull { it.id == scheduleId } ?: throw ScheduleNotFoundException(SCHEDULE_NOT_FOUND + scheduleId)
        if (schedule.status == SCHEDULE_AVAILABLE) {
            schedule.status = SCHEDULE_PENDING
            schedule.userEmail = user.email
            user.schedules.add(schedule)
        } else {
            throw ScheduleNotAvailableException(SCHEDULE_NOT_AVAILABLE)
        }
        scheduleRepository.saveAndFlush(schedule)
        userRepository.saveAndFlush(user)
        return mentorRepository.saveAndFlush(mentor)
    }

    fun cancelSchedule(mentorId: Int, scheduleId: Int): Mentor {
        val mentor = read(mentorId)
        val schedule = mentor.schedules.firstOrNull { it.id == scheduleId } ?: throw ScheduleNotFoundException(SCHEDULE_NOT_FOUND + scheduleId)
        if (schedule.status == SCHEDULE_AVAILABLE) {
            schedule.status = SCHEDULE_CANCELLED
        } else {
            throw CanOnlyCancelScheduleAvailableException(CAN_ONLY_CANCEL_SCHEDULE_AVAILABLE)
        }
        scheduleRepository.saveAndFlush(schedule)
        return mentorRepository.saveAndFlush(mentor)
    }

    fun approveSchedule(mentorId: Int, scheduleId: Int): Mentor {
        val mentor = read(mentorId)
        val schedule = mentor.schedules.firstOrNull { it.id == scheduleId } ?: throw ScheduleNotFoundException(SCHEDULE_NOT_FOUND + scheduleId)
        if (schedule.status == SCHEDULE_PENDING) {
            schedule.status = SCHEDULE_RESERVED
        } else {
            throw ScheduleNotPendingException(SCHEDULE_NOT_PENDING)
        }
        return mentorRepository.saveAndFlush(mentor)
    }

    private fun validateUserEqualsMentor(mentor: Mentor, user: User) {
        if (mentor.user == user) {
            throw MentorAndUserAreTheSameException(MENTOR_AND_USER_ARE_THE_SAME)
        }
    }
}
