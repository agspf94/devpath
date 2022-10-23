package com.devpath.exception

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionControllerAdvice {
    @ExceptionHandler
    fun handleUserAlreadyExistsException(e: UserAlreadyExistsException): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage(e.message), BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleNotSuchElementException(e: NoSuchElementException): ResponseEntity<ErrorMessage> {
        return ResponseEntity(ErrorMessage(e.message), NOT_FOUND)
    }

//    @ExceptionHandler(
//        EmptyListException::class,
//        TrailAlreadyExists::class,
//        TopicAlreadyExists::class,
//        SubTopicAlreadyExists::class
//    )
//    fun handleEmptyListException(e: Exception): ResponseEntity<ErrorMessage> {
//        return ResponseEntity(ErrorMessage(e.message), OK)
//    }
}
