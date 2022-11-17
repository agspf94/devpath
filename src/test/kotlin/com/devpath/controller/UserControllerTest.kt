package com.devpath.controller

import com.devpath.constants.Constants.Companion.SUB_TOPIC_NOT_FOUND
import com.devpath.constants.Constants.Companion.TOPIC_NOT_FOUND_ID
import com.devpath.constants.Constants.Companion.TRAIL_NOT_FOUND
import com.devpath.constants.Constants.Companion.USER_ALREADY_EXISTS
import com.devpath.constants.Constants.Companion.USER_DELETED
import com.devpath.constants.Constants.Companion.USER_NOT_FOUND_EMAIL
import com.devpath.constants.Constants.Companion.USER_NOT_FOUND_ID
import com.devpath.constants.Constants.Companion.USER_TRAIL_DELETED
import com.devpath.dto.user.response.DeleteUserResponse
import com.devpath.dto.user.response.DeleteUserTrailResponse
import com.devpath.entity.UserTrail
import com.devpath.exception.ErrorMessage
import com.devpath.exception.exceptions.UserAlreadyExistsException
import com.devpath.mocks.TrailMockProvider.Companion.getTrail
import com.devpath.mocks.UserMockProvider.Companion.getCreateUserRequest
import com.devpath.mocks.UserMockProvider.Companion.getUpdateTrailStatusRequest
import com.devpath.mocks.UserMockProvider.Companion.getUser
import com.devpath.mocks.UserMockProvider.Companion.getUpdateUserRequest
import com.devpath.service.UserService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(UserController::class)
class UserControllerTest {
    @MockBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userController: UserController

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `User controller should not be null`() {
        assertNotNull(userController)
    }

    @Test
    fun `Should create an user successfully`() {
        val createUserRequest = getCreateUserRequest()
        val user = getUser(id = 1)

        `when`(userService.create(createUserRequest)).thenReturn(user)

        mockMvc.perform(
                post("/user/create")
                    .accept(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(createUserRequest))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(user.toUserDTO())))

        verify(userService, times(1)).create(createUserRequest)
    }

    @Test
    fun `Should fail while creating an user with an email that already exists`() {
        val createUserRequest = getCreateUserRequest()
        val errorMessage = USER_ALREADY_EXISTS + createUserRequest.email

        `when`(userService.create(createUserRequest)).thenAnswer { throw UserAlreadyExistsException(errorMessage) }

        mockMvc.perform(
                post("/user/create")
                    .accept(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(createUserRequest))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(userService, times(1)).create(createUserRequest)
    }

    @Test
    fun `Should read an user successfully`() {
        val user = getUser(id = 1)

        `when`(userService.read(user.email)).thenReturn(user)

        mockMvc.perform(
                get("/user/${user.email}")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(user.toUserDTO())))

        verify(userService, times(1)).read(user.email)
    }

    @Test
    fun `Should fail while reading an user that doesn't exist`() {
        val user = getUser(id = 1)
        val errorMessage = USER_NOT_FOUND_EMAIL + user.email

        `when`(userService.read(user.email)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
            get("/user/${user.email}")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(userService, times(1)).read(user.email)
    }

    @Test
    fun `Should update an user successfully`() {
        val updateUserRequest = getUpdateUserRequest(id = 1)
        val user = getUser(id = 1)

        `when`(userService.update(updateUserRequest)).thenReturn(user)

        mockMvc.perform(
                patch("/user/update")
                    .accept(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(updateUserRequest))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(user.toUserDTO())))

        verify(userService, times(1)).update(updateUserRequest)
    }

    @Test
    fun `Should fail while updating an user that doesn't exist`() {
        val updateUserRequest = getUpdateUserRequest(id = 1)
        val errorMessage = USER_NOT_FOUND_ID + updateUserRequest.id

        `when`(userService.update(updateUserRequest)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
                patch("/user/update")
                    .accept(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(updateUserRequest))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(userService, times(1)).update(updateUserRequest)
    }

    @Test
    fun `Should delete an user successfully`() {
        val user = getUser(id = 1)
        val deleteUserResponse = DeleteUserResponse(user, USER_DELETED)

        `when`(userService.delete(user.email)).thenReturn(deleteUserResponse)

        mockMvc.perform(
                delete("/user/delete/${user.email}")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(deleteUserResponse)))

        verify(userService, times(1)).delete(user.email)
    }
    
    @Test
    fun `Should fail while deleting an user that doesn't exist`() {
        val user = getUser(id = 1)
        val errorMessage = USER_NOT_FOUND_EMAIL + user.email

        `when`(userService.delete(user.email)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
                delete("/user/delete/${user.email}")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(userService, times(1)).delete(user.email)
    }

    @Test
    fun `Should add a trail to an user successfully`() {
        val trail = getTrail(id = 1)
        val user = getUser(id = 1)
        user.userTrails.add(UserTrail(trail = trail, userTopics = mutableSetOf()))

        `when`(userService.addTrail(user.email, trail.id!!)).thenReturn(user)

        mockMvc.perform(
            post("/user/${user.email}/add-trail/${trail.id}")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(user.toUserDTO())))

        verify(userService, times(1)).addTrail(user.email, trail.id!!)
    }

    @Test
    fun `Should fail while adding a trail to an user that doesn't exist`() {
        val trail = getTrail(id = 1)
        val user = getUser(id = 1)
        val errorMessage = USER_NOT_FOUND_EMAIL + user.email

        `when`(userService.addTrail(user.email, trail.id!!)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
            post("/user/${user.email}/add-trail/${trail.id}")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(userService, times(1)).addTrail(user.email, trail.id!!)
    }

    @Test
    fun `Should fail while adding a trail to an user but the trail doesn't exist`() {
        val trail = getTrail(id = 1)
        val user = getUser(id = 1)
        val errorMessage = TRAIL_NOT_FOUND + trail.id

        `when`(userService.addTrail(user.email, trail.id!!)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
            post("/user/${user.email}/add-trail/${trail.id}")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(userService, times(1)).addTrail(user.email, trail.id!!)
    }

    @Test
    fun `Should update a trail status of an user successfully`() {
        val updateTrailStatusRequest = getUpdateTrailStatusRequest()
        val user = getUser(id = 1)

        `when`(userService.updateTrailStatus(updateTrailStatusRequest)).thenReturn(user)

        mockMvc.perform(
            patch("/user/update-trail")
                .accept(APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(updateTrailStatusRequest))
                .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(user.toUserDTO())))

        verify(userService, times(1)).updateTrailStatus(updateTrailStatusRequest)
    }

    @Test
    fun `Should fail while updating a trail status of an user that doesn't exist`() {
        val updateTrailStatusRequest = getUpdateTrailStatusRequest()
        val errorMessage = USER_NOT_FOUND_EMAIL + updateTrailStatusRequest.userEmail

        `when`(userService.updateTrailStatus(updateTrailStatusRequest)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
            patch("/user/update-trail")
                .accept(APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(updateTrailStatusRequest))
                .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(userService, times(1)).updateTrailStatus(updateTrailStatusRequest)
    }

    @Test
    fun `Should fail while updating a trail status of an user but the trail that doesn't exist`() {
        val updateTrailStatusRequest = getUpdateTrailStatusRequest()
        val errorMessage = TRAIL_NOT_FOUND + updateTrailStatusRequest.trailId

        `when`(userService.updateTrailStatus(updateTrailStatusRequest)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
            patch("/user/update-trail")
                .accept(APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(updateTrailStatusRequest))
                .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(userService, times(1)).updateTrailStatus(updateTrailStatusRequest)
    }

    @Test
    fun `Should fail while updating a trail status of an user but the topic that doesn't exist`() {
        val updateTrailStatusRequest = getUpdateTrailStatusRequest()
        val errorMessage = TOPIC_NOT_FOUND_ID + updateTrailStatusRequest.topicId

        `when`(userService.updateTrailStatus(updateTrailStatusRequest)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
            patch("/user/update-trail")
                .accept(APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(updateTrailStatusRequest))
                .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(userService, times(1)).updateTrailStatus(updateTrailStatusRequest)
    }

    @Test
    fun `Should fail while updating a trail status of an user but the sub topic that doesn't exist`() {
        val updateTrailStatusRequest = getUpdateTrailStatusRequest()
        val errorMessage = SUB_TOPIC_NOT_FOUND + updateTrailStatusRequest.subTopicId

        `when`(userService.updateTrailStatus(updateTrailStatusRequest)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
            patch("/user/update-trail")
                .accept(APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(updateTrailStatusRequest))
                .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(userService, times(1)).updateTrailStatus(updateTrailStatusRequest)
    }

    @Test
    fun `Should delete a trail from an user successfully`() {
        val user = getUser(id = 1)
        val trailId = 1
        val deleteUserTrailResponse = DeleteUserTrailResponse(user, USER_TRAIL_DELETED)

        `when`(userService.deleteTrail(user.email, trailId)).thenReturn(deleteUserTrailResponse)

        mockMvc.perform(
            delete("/user/${user.email}/delete-trail/${trailId}")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(deleteUserTrailResponse)))

        verify(userService, times(1)).deleteTrail(user.email, trailId)
    }

    @Test
    fun `Should fail while deleting a trail from an user that doesn't exist`() {
        val user = getUser(id = 1)
        val trailId = 1
        val errorMessage = USER_NOT_FOUND_EMAIL + user.email

        `when`(userService.deleteTrail(user.email, trailId)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
            delete("/user/${user.email}/delete-trail/${trailId}")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(userService, times(1)).deleteTrail(user.email, trailId)
    }

    @Test
    fun `Should fail while deleting a trail from an user but the trail doesn't exist`() {
        val user = getUser(id = 1)
        val trailId = 1
        val errorMessage = TRAIL_NOT_FOUND + trailId

        `when`(userService.deleteTrail(user.email, trailId)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
            delete("/user/${user.email}/delete-trail/${trailId}")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(userService, times(1)).deleteTrail(user.email, trailId)
    }
}
