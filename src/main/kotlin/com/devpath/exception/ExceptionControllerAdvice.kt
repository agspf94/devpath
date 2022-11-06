package com.devpath.exception

import com.devpath.exception.exceptions.EmptyTrailListException
import com.devpath.exception.exceptions.TrailAlreadyExistsException
import com.devpath.exception.exceptions.UserAlreadyExistsException
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionControllerAdvice {
    @ExceptionHandler(
        UserAlreadyExistsException::class,
        TrailAlreadyExistsException::class
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
        EmptyTrailListException::class
    )
    fun handleNoContent(e: Exception): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage(e.message), OK)
    }
}