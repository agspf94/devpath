package com.devpath.service

import com.devpath.constants.Constants.Companion.USER_ALREADY_EXISTS
import com.devpath.constants.Constants.Companion.USER_DELETED
import com.devpath.constants.Constants.Companion.USER_NOT_FOUND_EMAIL
import com.devpath.constants.Constants.Companion.USER_NOT_FOUND_ID
import com.devpath.constants.Constants.Companion.USER_TRAIL_DELETED
import com.devpath.dto.user.request.CreateUserRequest
import com.devpath.dto.user.request.UpdateTrailStatusRequest
import com.devpath.dto.user.request.UpdateUserRequest
import com.devpath.dto.user.response.DeleteUserResponse
import com.devpath.dto.user.response.DeleteUserTrailResponse
import com.devpath.entity.Trail
import com.devpath.entity.User
import com.devpath.entity.UserSubTopic
import com.devpath.entity.UserTopic
import com.devpath.entity.UserTrail
import com.devpath.exception.exceptions.UserAlreadyExistsException
import com.devpath.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val trailService: TrailService,
    private val topicService: TopicService,
    private val subTopicService: SubTopicService
) {
    fun create(createUserRequest: CreateUserRequest): User {
        userRepository.findByEmail(createUserRequest.email)
            .ifPresent { throw UserAlreadyExistsException(USER_ALREADY_EXISTS + createUserRequest.email) }
        return userRepository.saveAndFlush(createUserRequest.toUser()).formatResponse()
    }

    fun read(email: String): User {
        return userRepository.findByEmail(email)
            .map { it.formatResponse() }
            .orElseThrow { NoSuchElementException(USER_NOT_FOUND_EMAIL + email) }
    }

    fun update(updateUserRequest: UpdateUserRequest): User {
        return userRepository.findById(updateUserRequest.id)
            .map {
                userRepository.saveAndFlush(
                    User(
                        id = it.id,
                        name = updateUserRequest.name ?: it.name,
                        email = updateUserRequest.email ?: it.email,
                        password = updateUserRequest.password ?: it.password,
                        isMentor = updateUserRequest.isMentor ?: it.isMentor,
                        userTrails = it.userTrails
                    ).formatResponse()
                )
            }
            .orElseThrow { NoSuchElementException(USER_NOT_FOUND_ID + updateUserRequest.id) }
    }

    fun delete(email: String): DeleteUserResponse {
        return userRepository.findByEmail(email)
            .map {
                userRepository.deleteById(it.id!!)
                DeleteUserResponse(it, USER_DELETED)
            }
            .orElseThrow { NoSuchElementException(USER_NOT_FOUND_EMAIL + email) }
    }

    fun addTrail(userEmail: String, trailId: Int): User {
        val user = read(userEmail)
        val trail = trailService.read(trailId)
        user.userTrails.add(createUserTrail(trail))
        userRepository.saveAndFlush(user)
        return user.formatResponse()
    }

    fun deleteTrail(userEmail: String, trailId: Int): DeleteUserTrailResponse {
        val user = read(userEmail)
        val trail = trailService.read(trailId)
        user.userTrails.removeIf { it.trail.id == trail.id }
        userRepository.saveAndFlush(user)
        return DeleteUserTrailResponse(
            user.formatResponse(),
            USER_TRAIL_DELETED
        )
    }

    private fun createUserTrail(trail: Trail): UserTrail {
        return UserTrail(
            trail = trail,
            userTopics = trail.topics.map { topic ->
                UserTopic(
                    topic = topic,
                    userSubTopics = topic.subTopics.map { subTopic ->
                        UserSubTopic(
                            subTopic = subTopic,
                            active = false
                        )
                    }.toMutableSet()
                )
            }.toMutableSet()
        )
    }

    fun updateTrailStatus(updateTrailStatusRequest: UpdateTrailStatusRequest): User {
        val user = read(updateTrailStatusRequest.userEmail)
        validateUpdateTrailStatusRequest(updateTrailStatusRequest)
        user.userTrails.first { it.trail.id == updateTrailStatusRequest.trailId }
            .userTopics.first { it.topic.id == updateTrailStatusRequest.topicId }
            .userSubTopics.first { it.subTopic.id == updateTrailStatusRequest.subTopicId }
            .active = updateTrailStatusRequest.active
        userRepository.saveAndFlush(user)
        return user.formatResponse()
    }

    private fun validateUpdateTrailStatusRequest(updateTrailStatusRequest: UpdateTrailStatusRequest) {
        trailService.read(updateTrailStatusRequest.trailId)
        topicService.read(updateTrailStatusRequest.topicId)
        subTopicService.read(updateTrailStatusRequest.subTopicId)
    }

    private fun User.formatResponse(): User {
        this.userTrails = this.userTrails.sortedBy { it.trail.id }.toMutableSet()
        this.userTrails.map { userTrail ->
            userTrail.userTopics = userTrail.userTopics.sortedBy { userTopic -> userTopic.topic.id }.toMutableSet()
            userTrail.userTopics.map { userTopic ->
                userTopic.userSubTopics = userTopic.userSubTopics.sortedBy { userSubTopic -> userSubTopic.subTopic.id }.toMutableSet()
            }
        }
        return this
    }
}
