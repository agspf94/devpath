package com.devpath.service

import com.devpath.constants.Constants.Companion.SUB_TOPIC_NOT_FOUND
import com.devpath.constants.Constants.Companion.TOPIC_NOT_FOUND_ID
import com.devpath.constants.Constants.Companion.TRAIL_ALREADY_EXISTS
import com.devpath.constants.Constants.Companion.TRAIL_NOT_FOUND
import com.devpath.constants.Constants.Companion.USER_ALREADY_EXISTS
import com.devpath.constants.Constants.Companion.USER_DELETED
import com.devpath.constants.Constants.Companion.USER_DOES_NOT_HAVE_TRAIL
import com.devpath.constants.Constants.Companion.USER_NOT_FOUND_EMAIL
import com.devpath.constants.Constants.Companion.USER_NOT_FOUND_ID
import com.devpath.constants.Constants.Companion.USER_TRAIL_DELETED
import com.devpath.constants.Constants.Companion.USER_WRONG_PASSWORD
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
import com.devpath.exception.exceptions.trail.TrailAlreadyExistsException
import com.devpath.exception.exceptions.user.UserAlreadyExistsException
import com.devpath.exception.exceptions.user.WrongPasswordException
import com.devpath.repository.UserRepository
import com.devpath.repository.UserTrailRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val trailService: TrailService,
    private val topicService: TopicService,
    private val subTopicService: SubTopicService,
    private val userTrailRepository: UserTrailRepository
) {
    fun create(createUserRequest: CreateUserRequest): User {
        userRepository.findByEmail(createUserRequest.email)
            .ifPresent { throw UserAlreadyExistsException(USER_ALREADY_EXISTS + createUserRequest.email) }
        return userRepository.saveAndFlush(createUserRequest.toUser()).formatResponse()
    }

    fun login(email: String, password: String): User {
        return userRepository.findByEmail(email)
            .map {
                if (it.password == password) {
                    it.formatResponse()
                } else {
                    throw WrongPasswordException(USER_WRONG_PASSWORD)
                }
            }
            .orElseThrow { NoSuchElementException(USER_NOT_FOUND_EMAIL + email) }
    }

    fun read(email: String): User {
        return userRepository.findByEmail(email)
            .map { it.formatResponse() }
            .orElseThrow { NoSuchElementException(USER_NOT_FOUND_EMAIL + email) }
    }

    fun update(updateUserRequest: UpdateUserRequest): User {
        return userRepository.findById(updateUserRequest.id)
            .map {
                val updatedUser = User(
                    id = it.id,
                    name = updateUserRequest.name ?: it.name,
                    email = updateUserRequest.email ?: it.email,
                    password = updateUserRequest.password ?: it.password,
                    mentorStatus = updateUserRequest.mentorStatus ?: it.mentorStatus,
                    userTrails = it.userTrails
                )
                userRepository.saveAndFlush(updatedUser)
                updatedUser.formatResponse()
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
        if (user.userTrails.firstOrNull { it.trail.id == trailId } == null) {
            user.userTrails.add(createUserTrail(trail))
            userRepository.saveAndFlush(user)
        } else {
            throw TrailAlreadyExistsException(TRAIL_ALREADY_EXISTS + trail.name)
        }
        return user.formatResponse()
    }

    fun updateTrailStatus(updateTrailStatusRequest: UpdateTrailStatusRequest): User {
        val user = read(updateTrailStatusRequest.userEmail)
        validateUpdateTrailStatusRequest(user, updateTrailStatusRequest)
        user.userTrails.first { it.trail.id == updateTrailStatusRequest.trailId }
            .userTopics.first { it.topic.id == updateTrailStatusRequest.topicId }
            .userSubTopics.first { it.subTopic.id == updateTrailStatusRequest.subTopicId }
            .active = updateTrailStatusRequest.active
        userRepository.saveAndFlush(user)
        return user.formatResponse()
    }

    fun deleteTrail(userEmail: String, trailId: Int): DeleteUserTrailResponse {
        val user = read(userEmail)
        val trail = trailService.read(trailId)
        val userTrail = user.userTrails.firstOrNull { it.trail.id == trail.id } ?: throw NoSuchElementException(USER_DOES_NOT_HAVE_TRAIL + trailId)
        user.userTrails.removeIf { it.trail.id == trail.id }
        userRepository.saveAndFlush(user)
        userTrailRepository.delete(userTrail)
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

    private fun validateUpdateTrailStatusRequest(user: User, updateTrailStatusRequest: UpdateTrailStatusRequest) {
        val userTrail = user.userTrails.firstOrNull { it.trail.id == updateTrailStatusRequest.trailId } ?: throw NoSuchElementException(TRAIL_NOT_FOUND + updateTrailStatusRequest.trailId)
        trailService.read(userTrail.trail.id!!)
        val userTopic = userTrail.userTopics.firstOrNull { it.topic.id == updateTrailStatusRequest.topicId } ?: throw NoSuchElementException(TOPIC_NOT_FOUND_ID + updateTrailStatusRequest.topicId)
        topicService.read(userTopic.topic.id!!)
        val userSubTopic = userTopic.userSubTopics.firstOrNull { it.subTopic.id == updateTrailStatusRequest.subTopicId } ?: throw NoSuchElementException(SUB_TOPIC_NOT_FOUND + updateTrailStatusRequest.subTopicId)
        subTopicService.read(userSubTopic.subTopic.id!!)
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
