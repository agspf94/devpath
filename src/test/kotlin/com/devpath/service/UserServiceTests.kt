package com.devpath.service

import com.devpath.constants.Constants
import com.devpath.constants.Constants.Companion.SUB_TOPIC_NOT_FOUND
import com.devpath.constants.Constants.Companion.TOPIC_NOT_FOUND_ID
import com.devpath.constants.Constants.Companion.TRAIL_NOT_FOUND
import com.devpath.constants.Constants.Companion.USER_ALREADY_EXISTS
import com.devpath.constants.Constants.Companion.USER_DELETED
import com.devpath.constants.Constants.Companion.USER_NOT_FOUND_EMAIL
import com.devpath.constants.Constants.Companion.USER_NOT_FOUND_ID
import com.devpath.constants.Constants.Companion.USER_TRAIL_DELETED
import com.devpath.constants.Constants.Companion.USER_WRONG_PASSWORD
import com.devpath.entity.User
import com.devpath.exception.exceptions.trail.TrailAlreadyExistsException
import com.devpath.exception.exceptions.user.UserAlreadyExistsException
import com.devpath.exception.exceptions.user.WrongPasswordException
import com.devpath.mock.TrailMockProvider.Companion.getTrail
import com.devpath.mock.UserMockProvider.Companion.getCreateUserRequest
import com.devpath.mock.UserMockProvider.Companion.getUpdateTrailStatusRequest
import com.devpath.mock.UserMockProvider.Companion.getUpdateUserRequest
import com.devpath.mock.UserMockProvider.Companion.getUser
import com.devpath.mock.UserTrailMockProvider.Companion.getCompletedUserTrail
import com.devpath.mock.UserTrailMockProvider.Companion.getUserTrail
import com.devpath.repository.UserRepository
import com.devpath.repository.UserTrailRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.Optional

@SpringBootTest
class UserServiceTests {
    @MockBean
    private lateinit var userRepository: UserRepository

    @MockBean
    private lateinit var trailService: TrailService

    @MockBean
    private lateinit var topicService: TopicService

    @MockBean
    private lateinit var subTopicService: SubTopicService

    @MockBean
    private lateinit var userTrailRepository: UserTrailRepository

    @Autowired
    private lateinit var userService: UserService

    @Test
    fun `User service should not be null`() {
        assertNotNull(userService)
    }

    @Test
    fun `Create user should return the new user`() {
        val createUserRequest = getCreateUserRequest()
        val user = getUser(id = 1)

        `when`(userRepository.findByEmail(createUserRequest.email)).thenReturn(Optional.empty())
        `when`(userRepository.saveAndFlush(any())).thenReturn(user)

        val savedUser = userService.create(createUserRequest)

        assertAttributes(user, savedUser)

        verify(userRepository, times(1)).findByEmail(createUserRequest.email)
        verify(userRepository, times(1)).saveAndFlush(any())
    }

    @Test
    fun `Create user should throw an exception when the user already exists`() {
        val createUserRequest = getCreateUserRequest()
        val user = getUser(id = 1)

        `when`(userRepository.findByEmail(createUserRequest.email)).thenReturn(Optional.of(user))

        val exception = assertThrows<UserAlreadyExistsException> { userService.create(createUserRequest) }
        assertEquals(USER_ALREADY_EXISTS + createUserRequest.email, exception.message)

        verify(userRepository, times(1)).findByEmail(createUserRequest.email)
        verify(userRepository, times(0)).saveAndFlush(any())
    }

    @Test
    fun `Login should return the desired user`() {
        val user = getUser(id = 1)

        `when`(userRepository.findByEmail(user.email)).thenReturn(Optional.of(user))

        val loggedUser = userService.login(user.email, user.password)

        assertAttributes(user, loggedUser)

        verify(userRepository, times(1)).findByEmail(user.email)
    }

    @Test
    fun `Login should throw an exception when the user's password is wrong`() {
        val user = getUser(id = 1)

        `when`(userRepository.findByEmail(user.email)).thenReturn(Optional.of(user))

        val exception = assertThrows<WrongPasswordException> { userService.login(user.email, "wrongPassword") }
        assertEquals(USER_WRONG_PASSWORD, exception.message)

        verify(userRepository, times(1)).findByEmail(user.email)
    }

    @Test
    fun `Login should throw an exception when the user doesn't exist`() {
        val user = getUser(id = 1)

        `when`(userRepository.findByEmail(user.email)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> { userService.login(user.email, user.password) }
        assertEquals(USER_NOT_FOUND_EMAIL + user.email, exception.message)

        verify(userRepository, times(1)).findByEmail(user.email)
    }

    @Test
    fun `Read should return the desired user`() {
        val user = getUser(id = 1)

        `when`(userRepository.findByEmail(user.email)).thenReturn(Optional.of(user))

        val savedUser = userService.read(user.email)

        assertAttributes(user, savedUser)

        verify(userRepository, times(1)).findByEmail(user.email)
    }

    @Test
    fun `Read should throw an exception when the user doesn't exist`() {
        val user = getUser(id = 1)

        `when`(userRepository.findByEmail(user.email)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> { userService.read(user.email) }
        assertEquals(USER_NOT_FOUND_EMAIL + user.email, exception.message)

        verify(userRepository, times(1)).findByEmail(user.email)
    }

    @Test
    fun `Update should update the user and return it updated`() {
        val updateUserRequest = getUpdateUserRequest(id = 1)
        val user = getUser(id = 1)

        `when`(userRepository.findById(updateUserRequest.id)).thenReturn(Optional.of(user))
        `when`(userRepository.saveAndFlush(any())).thenReturn(user)

        val updatedUser = userService.update(updateUserRequest)

        assertEquals(updateUserRequest.name, updatedUser.name)
        assertEquals(updateUserRequest.email, updatedUser.email)
        assertEquals(updateUserRequest.password, updatedUser.password)
        assertEquals(updateUserRequest.mentorStatus, updatedUser.mentorStatus)

        verify(userRepository, times(1)).findById(updateUserRequest.id)
        verify(userRepository, times(1)).saveAndFlush(any())
    }

    @Test
    fun `Update should throw an exception when the user doesn't exist`() {
        val updateUserRequest = getUpdateUserRequest(id = 1)

        `when`(userRepository.findById(updateUserRequest.id)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> { userService.update(updateUserRequest) }
        assertEquals(USER_NOT_FOUND_ID + updateUserRequest.id, exception.message)

        verify(userRepository, times(1)).findById(updateUserRequest.id)
        verify(userRepository, times(0)).saveAndFlush(any())
    }

    @Test
    fun `Delete should delete the desired user`() {
        val user = getUser(id = 1)

        `when`(userRepository.findByEmail(user.email)).thenReturn(Optional.of(user))
        doNothing().`when`(userRepository).deleteById(user.id!!)

        val deleteUserResponse = userService.delete(user.email)

        assertEquals(user, deleteUserResponse.user)
        assertEquals(USER_DELETED, deleteUserResponse.message)

        verify(userRepository, times(1)).findByEmail(user.email)
        verify(userRepository, times(1)).deleteById(user.id!!)
    }

    @Test
    fun `Delete should throw an exception when the user doesn't exist`() {
        val user = getUser(id = 1)

        `when`(userRepository.findByEmail(user.email)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> { userService.delete(user.email) }
        assertEquals(USER_NOT_FOUND_EMAIL + user.email, exception.message)

        verify(userRepository, times(1)).findByEmail(user.email)
        verify(userRepository, times(0)).deleteById(any())
    }

    @Test
    fun `Add trail should add a trail to an user`() {
        val user = getUser(id = 1)
        val trail = getTrail(id = 1)
        val userTrail = getUserTrail(id = 1)

        `when`(userRepository.findByEmail(user.email)).thenReturn(Optional.of(user))
        `when`(trailService.read(trail.id!!)).thenReturn(trail)
        `when`(userRepository.saveAndFlush(user)).thenReturn(user)

        val savedUser = userService.addTrail(user.email, trail.id!!)
        user.userTrails.add(userTrail)

        assertAttributes(user, savedUser)

        verify(userRepository, times(1)).findByEmail(user.email)
        verify(trailService, times(1)).read(trail.id!!)
        verify(userRepository, times(1)).saveAndFlush(user)
    }

    @Test
    fun `Add trail should throw an exception when the user doesn't exist`() {
        val user = getUser(id = 1)
        val trail = getTrail(id = 1)

        `when`(userRepository.findByEmail(user.email)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> { userService.addTrail(user.email, trail.id!!) }
        assertEquals(USER_NOT_FOUND_EMAIL + user.email, exception.message)

        verify(userRepository, times(1)).findByEmail(user.email)
        verify(trailService, times(0)).read(trail.id!!)
        verify(userRepository, times(0)).saveAndFlush(any())
    }

    @Test
    fun `Add trail should throw an exception when the user already added that trail`() {
        val user = getUser(id = 1)
        val trail = getTrail(id = 1)
        val userTrail = getUserTrail(id = 1)
        user.userTrails.add(userTrail)

        `when`(userRepository.findByEmail(user.email)).thenReturn(Optional.of(user))
        `when`(trailService.read(trail.id!!)).thenReturn(trail)

        val exception = assertThrows<TrailAlreadyExistsException> { userService.addTrail(user.email, trail.id!!) }
        assertEquals(Constants.TRAIL_ALREADY_EXISTS + trail.name, exception.message)

        verify(userRepository, times(1)).findByEmail(user.email)
        verify(trailService, times(1)).read(trail.id!!)
        verify(userRepository, times(0)).saveAndFlush(any())
    }

    @Test
    fun `Update trail status should update the sub topic status and return the updated user`() {
        val updateTrailStatusRequest = getUpdateTrailStatusRequest()
        val user = getUser(id = 1)
        val userTrail = getCompletedUserTrail(id = 1)
        user.userTrails.add(userTrail)

        `when`(userRepository.findByEmail(updateTrailStatusRequest.userEmail)).thenReturn(Optional.of(user))
        `when`(trailService.read(user.userTrails.first().trail.id!!)).thenReturn(any())
        `when`(topicService.read(user.userTrails.first().userTopics.first().id!!)).thenReturn(any())
        `when`(subTopicService.read(user.userTrails.first().userTopics.first().userSubTopics.first().id!!)).thenReturn(any())

        val updatedUser = userService.updateTrailStatus(updateTrailStatusRequest)

        assertEquals(true, updatedUser.userTrails.first().userTopics.first().userSubTopics.first().active)

        verify(userRepository, times(1)).findByEmail(updateTrailStatusRequest.userEmail)
        verify(trailService, times(1)).read(user.userTrails.first().trail.id!!)
        verify(topicService, times(1)).read(user.userTrails.first().userTopics.first().id!!)
        verify(subTopicService, times(1)).read(user.userTrails.first().userTopics.first().userSubTopics.first().id!!)
        verify(userRepository, times(1)).saveAndFlush(any())
    }

    @Test
    fun `Update trail status should throw an exception when the user doesn't exist`() {
        val updateTrailStatusRequest = getUpdateTrailStatusRequest()

        `when`(userRepository.findByEmail(updateTrailStatusRequest.userEmail)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> { userService.updateTrailStatus(updateTrailStatusRequest) }
        assertEquals(USER_NOT_FOUND_EMAIL + updateTrailStatusRequest.userEmail, exception.message)

        verify(userRepository, times(1)).findByEmail(updateTrailStatusRequest.userEmail)
        verify(userRepository, times(0)).saveAndFlush(any())
    }

    @Test
    fun `Update trail status should throw an exception when the user doesn't have that trail`() {
        val updateTrailStatusRequest = getUpdateTrailStatusRequest(trailId = 2)
        val user = getUser(id = 1)
        val userTrail = getCompletedUserTrail(id = 1)
        user.userTrails.add(userTrail)

        `when`(userRepository.findByEmail(updateTrailStatusRequest.userEmail)).thenReturn(Optional.of(user))

        val exception = assertThrows<NoSuchElementException> { userService.updateTrailStatus(updateTrailStatusRequest) }
        assertEquals(TRAIL_NOT_FOUND + updateTrailStatusRequest.trailId, exception.message)

        verify(userRepository, times(1)).findByEmail(updateTrailStatusRequest.userEmail)
        verify(userRepository, times(0)).saveAndFlush(any())
    }

    @Test
    fun `Update trail status should throw an exception when the trail doesn't have that topic`() {
        val updateTrailStatusRequest = getUpdateTrailStatusRequest(topicId = 2)
        val user = getUser(id = 1)
        val userTrail = getCompletedUserTrail(id = 1)
        user.userTrails.add(userTrail)

        `when`(userRepository.findByEmail(updateTrailStatusRequest.userEmail)).thenReturn(Optional.of(user))

        val exception = assertThrows<NoSuchElementException> { userService.updateTrailStatus(updateTrailStatusRequest) }
        assertEquals(TOPIC_NOT_FOUND_ID + updateTrailStatusRequest.topicId, exception.message)

        verify(userRepository, times(1)).findByEmail(updateTrailStatusRequest.userEmail)
        verify(userRepository, times(0)).saveAndFlush(any())
    }

    @Test
    fun `Update trail status should throw an exception when the topic doesn't have that sub topic`() {
        val updateTrailStatusRequest = getUpdateTrailStatusRequest(subTopicId = 3)
        val user = getUser(id = 1)
        val userTrail = getCompletedUserTrail(id = 1)
        user.userTrails.add(userTrail)

        `when`(userRepository.findByEmail(updateTrailStatusRequest.userEmail)).thenReturn(Optional.of(user))

        val exception = assertThrows<NoSuchElementException> { userService.updateTrailStatus(updateTrailStatusRequest) }
        assertEquals(SUB_TOPIC_NOT_FOUND + updateTrailStatusRequest.subTopicId, exception.message)

        verify(userRepository, times(1)).findByEmail(updateTrailStatusRequest.userEmail)
        verify(userRepository, times(0)).saveAndFlush(any())
    }

    @Test
    fun `Delete trail should delete that trail from the user trail list`() {
        val user = getUser(id = 1)
        val trail = getTrail(id = 1)
        val userTrail = getCompletedUserTrail(id = 1)
        user.userTrails.add(userTrail)

        `when`(userRepository.findByEmail(user.email)).thenReturn(Optional.of(user))
        `when`(trailService.read(trail.id!!)).thenReturn(trail)
        `when`(userRepository.saveAndFlush(user)).thenReturn(user)
        doNothing().`when`(userTrailRepository).delete(any())

        val deleteUserTrailResponse = userService.deleteTrail(user.email, trail.id!!)
        user.userTrails.remove(userTrail)

        assertEquals(user, deleteUserTrailResponse.user)
        assertEquals(USER_TRAIL_DELETED, deleteUserTrailResponse.message)

        verify(userRepository, times(1)).findByEmail(user.email)
        verify(trailService, times(1)).read(trail.id!!)
        verify(userRepository, times(1)).saveAndFlush(user)
        verify(userTrailRepository, times(1)).delete(userTrail)
    }

    @Test
    fun `Delete trail should throw an exception when the user doesn't exist`() {
        val user = getUser(id = 1)
        val trail = getTrail(id = 1)

        `when`(userRepository.findByEmail(user.email)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> { userService.deleteTrail(user.email, trail.id!!) }
        assertEquals(USER_NOT_FOUND_EMAIL + user.email, exception.message)

        verify(userRepository, times(1)).findByEmail(user.email)
        verify(userRepository, times(0)).saveAndFlush(any())
        verify(userTrailRepository, times(0)).delete(any())
    }

    private fun assertAttributes(expectedUser: User, savedUser: User) {
        assertNotNull(expectedUser.id)
        assertEquals(expectedUser.name, savedUser.name)
        assertEquals(expectedUser.email, savedUser.email)
        assertEquals(expectedUser.password, savedUser.password)
        assertEquals(expectedUser.mentorStatus, savedUser.mentorStatus)
        assertEquals(expectedUser.userTrails, savedUser.userTrails)
    }
}
