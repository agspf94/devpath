package com.devpath.exception

import com.devpath.exception.exceptions.EmptyJobListException
import com.devpath.exception.exceptions.EmptyMentorListException
import com.devpath.exception.exceptions.EmptySubTopicListException
import com.devpath.exception.exceptions.EmptyTopicListException
import com.devpath.exception.exceptions.EmptyTrailListException
import com.devpath.exception.exceptions.JobAlreadyExistsException
import com.devpath.exception.exceptions.SubTopicAlreadyExistsException
import com.devpath.exception.exceptions.TopicAlreadyExistsException
import com.devpath.exception.exceptions.TrailAlreadyExistsException
import com.devpath.exception.exceptions.UserAlreadyExistsException
import com.devpath.exception.exceptions.UserIsNotAMentorException
import com.devpath.exception.exceptions.WrongPasswordException
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionControllerAdvice {
    @ExceptionHandler(
        UserAlreadyExistsException::class,
        TrailAlreadyExistsException::class,
        UserIsNotAMentorException::class,
        JobAlreadyExistsException::class,
        TopicAlreadyExistsException::class,
        SubTopicAlreadyExistsException::class,
        WrongPasswordException::class
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
        EmptySubTopicListException::class
    )
    fun handleNoContent(e: Exception): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage(e.message), OK)
    }
}
