package com.devpath.service

import com.devpath.constants.Constants.Companion.USER_ALREADY_EXISTS
import com.devpath.constants.Constants.Companion.USER_DELETED
import com.devpath.constants.Constants.Companion.USER_NOT_FOUND_EMAIL
import com.devpath.constants.Constants.Companion.USER_NOT_FOUND_ID
import com.devpath.dto.user.request.CreateUserRequest
import com.devpath.dto.user.request.UpdateUserRequest
import com.devpath.dto.user.response.DeleteUserResponse
import com.devpath.entity.User
import com.devpath.exception.exceptions.UserAlreadyExistsException
import com.devpath.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val trailService: TrailService
) {
    fun createUser(createUserRequest: CreateUserRequest): User {
        userRepository.findByEmail(createUserRequest.email)
            .ifPresent { throw UserAlreadyExistsException(USER_ALREADY_EXISTS + createUserRequest.email) }
        return userRepository.saveAndFlush(createUserRequest.toUser())
    }

    fun readUser(email: String): User {
        return userRepository.findByEmail(email)
            .map { it }
            .orElseThrow { NoSuchElementException(USER_NOT_FOUND_EMAIL + email) }
    }

    fun updateUser(updateUserRequest: UpdateUserRequest): User {
        return userRepository.findById(updateUserRequest.id)
            .map {
                userRepository.saveAndFlush(
                    User(
                        id = it.id,
                        name = updateUserRequest.name ?: it.name,
                        email = updateUserRequest.email ?: it.email,
                        password = updateUserRequest.password ?: it.password,
                        isMentor = updateUserRequest.isMentor ?: it.isMentor,
                        trails = it.trails
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

    fun addTrail(userEmail: String, trailId: Int): User {
        val user = readUser(userEmail)
        val trail = trailService.readTrail(trailId)
        user.trails.add(trail)
        userRepository.saveAndFlush(user)
        return user
    }
}
