package com.devpath.service

import com.devpath.constants.Constants.Companion.USER_ALREADY_EXISTS
import com.devpath.constants.Constants.Companion.USER_DELETED
import com.devpath.constants.Constants.Companion.USER_NOT_FOUND_EMAIL
import com.devpath.constants.Constants.Companion.USER_NOT_FOUND_ID
import com.devpath.dto.user.request.CreateUserRequest
import com.devpath.dto.user.request.UpdateUserRequest
import com.devpath.dto.user.response.DeleteUserResponse
import com.devpath.entity.User
import com.devpath.exception.UserAlreadyExistsException
import com.devpath.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun createUser(createUserRequest: CreateUserRequest): User {
        userRepository.findByEmail(createUserRequest.email)
            .ifPresent { throw UserAlreadyExistsException(USER_ALREADY_EXISTS + createUserRequest.email) }
        return userRepository.save(createUserRequest.toUser())
    }

    fun getUser(email: String): User {
        return userRepository.findByEmail(email)
            .map { it }
            .orElseThrow { NoSuchElementException(USER_NOT_FOUND_EMAIL + email) }
    }

    fun updateUser(updateUserRequest: UpdateUserRequest): User {
        return userRepository.findById(updateUserRequest.id)
            .map {
                userRepository.save(
                    User(
                        id = it.id,
                        name = updateUserRequest.name ?: it.name,
                        email = updateUserRequest.email ?: it.email,
                        password = updateUserRequest.password ?: it.password,
                        isMentor = updateUserRequest.isMentor ?: it.isMentor
                    )
                )
            }
            .orElseThrow { NoSuchElementException(USER_NOT_FOUND_ID + updateUserRequest.id) }
    }

    fun deleteUser(email: String): DeleteUserResponse {
        return userRepository.findByEmail(email)
            .map {
                userRepository.deleteById(it.id!!)
                DeleteUserResponse(it, USER_DELETED)
            }
            .orElseThrow { NoSuchElementException(USER_NOT_FOUND_EMAIL + email) }
    }
}
