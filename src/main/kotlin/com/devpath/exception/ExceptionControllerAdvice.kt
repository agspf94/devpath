package com.devpath.exception

import com.devpath.exception.exceptions.job.EmptyJobListException
import com.devpath.exception.exceptions.job.JobAlreadyExistsException
import com.devpath.exception.exceptions.mentor.EmptyMentorListException
import com.devpath.exception.exceptions.mentor.MentorAndUserAreTheSameException
import com.devpath.exception.exceptions.mentor.UserDidntRequestToBecomeAMentorException
import com.devpath.exception.exceptions.mentor.UserIsNotAMentorException
import com.devpath.exception.exceptions.schedule.CanOnlyCancelScheduleAvailableException
import com.devpath.exception.exceptions.schedule.ScheduleNotAvailableException
import com.devpath.exception.exceptions.schedule.ScheduleNotFoundException
import com.devpath.exception.exceptions.schedule.ScheduleNotPendingException
import com.devpath.exception.exceptions.subtopic.EmptySubTopicListException
import com.devpath.exception.exceptions.subtopic.SubTopicAlreadyExistsException
import com.devpath.exception.exceptions.topic.EmptyTopicListException
import com.devpath.exception.exceptions.topic.TopicAlreadyExistsException
import com.devpath.exception.exceptions.trail.EmptyTrailListException
import com.devpath.exception.exceptions.trail.EmptyWordsListException
import com.devpath.exception.exceptions.trail.NoTrailsWereFoundException
import com.devpath.exception.exceptions.trail.TrailAlreadyExistsException
import com.devpath.exception.exceptions.user.UserAlreadyExistsException
import com.devpath.exception.exceptions.user.WrongPasswordException
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionControllerAdvice {
    @ExceptionHandler(
        // User
        UserAlreadyExistsException::class,
        WrongPasswordException::class,

        // Trail
        TrailAlreadyExistsException::class,
        NoTrailsWereFoundException::class,

        // Mentor
        UserIsNotAMentorException::class,
        UserDidntRequestToBecomeAMentorException::class,
        MentorAndUserAreTheSameException::class,

        // Job
        JobAlreadyExistsException::class,

        // Topic
        TopicAlreadyExistsException::class,

        // Sub Topic
        SubTopicAlreadyExistsException::class,

        // Schedule
        ScheduleNotFoundException::class,
        ScheduleNotAvailableException::class,
        ScheduleNotPendingException::class,
        CanOnlyCancelScheduleAvailableException::class
    )
    fun handleBadRequest(e: Exception): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage(e.message), BAD_REQUEST)
    }

    @ExceptionHandler(
        NoSuchElementException::class
    )
    fun handleNotFound(e: Exception): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage(e.message), NOT_FOUND)
    }

    @ExceptionHandler(
        EmptyTrailListException::class,
        EmptyMentorListException::class,
        EmptyJobListException::class,
        EmptyTopicListException::class,
        EmptySubTopicListException::class,
        EmptyWordsListException::class
    )
    fun handleNoContent(e: Exception): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage(e.message), OK)
    }
}
