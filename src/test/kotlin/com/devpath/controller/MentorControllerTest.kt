package com.devpath.controller

import com.devpath.constants.Constants.Companion.MENTOR_DELETED
import com.devpath.constants.Constants.Companion.MENTOR_LIST_IS_EMPTY
import com.devpath.constants.Constants.Companion.USER_IS_NOT_A_MENTOR
import com.devpath.constants.Constants.Companion.USER_NOT_FOUND_ID
import com.devpath.dto.mentor.response.DeleteMentorResponse
import com.devpath.exception.ErrorMessage
import com.devpath.exception.exceptions.EmptyMentorListException
import com.devpath.exception.exceptions.UserIsNotAMentorException
import com.devpath.mock.MentorMockProvider.Companion.getMentor
import com.devpath.mock.MentorMockProvider.Companion.getUpdateMentorRequest
import com.devpath.mock.UserMockProvider.Companion.getUser
import com.devpath.service.MentorService
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

@WebMvcTest(MentorController::class)
class MentorControllerTest {
    @MockBean
    private lateinit var mentorService: MentorService

    @Autowired
    private lateinit var mentorController: MentorController

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `Mentor controller should not be null`() {
        assertNotNull(mentorController)
    }

    @Test
    fun `Should become a mentor successfully`() {
        val user = getUser(id = 1)
        val mentor = getMentor(id = 1, user = user)

        `when`(mentorService.becomeMentor(user.id!!)).thenReturn(mentor)

        mockMvc.perform(
                post("/mentor/become-mentor/${user.id!!}")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(mentor)))

        verify(mentorService, times(1)).becomeMentor(user.id!!)
    }

    @Test
    fun `Should fail while becoming a mentor when the user doesn't exist`() {
        val userId = 1
        val errorMessage = USER_NOT_FOUND_ID + userId

        `when`(mentorService.becomeMentor(userId)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
                post("/mentor/become-mentor/$userId")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(mentorService, times(1)).becomeMentor(userId)
    }

    @Test
    fun `Should read a mentor successfully`() {
        val user = getUser(id = 1)
        val mentor = getMentor(id = 1, user = user)

        `when`(mentorService.read(user.id!!)).thenReturn(mentor)

        mockMvc.perform(
                get("/mentor/${mentor.id!!}")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(mentor)))

        verify(mentorService, times(1)).read(user.id!!)
    }

    @Test
    fun `Should fail while reading a mentor when the user is not a mentor`() {
        val user = getUser(id = 1)
        val errorMessage = USER_IS_NOT_A_MENTOR + user.id

        `when`(mentorService.read(user.id!!)).thenAnswer { throw UserIsNotAMentorException(errorMessage, user) }

        mockMvc.perform(
            get("/mentor/${user.id!!}")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(mentorService, times(1)).read(user.id!!)
    }

    @Test
    fun `Should fail while reading a mentor when the user doesn't exist`() {
        val user = getUser(id = 1)
        val errorMessage = USER_NOT_FOUND_ID + user.id

        `when`(mentorService.read(user.id!!)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
            get("/mentor/${user.id!!}")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(mentorService, times(1)).read(user.id!!)
    }

    @Test
    fun `Should read all mentors list successfully`() {
        val user1 = getUser(id = 1)
        val user2 = getUser(id = 2)
        val mentorsList = listOf(getMentor(id = 1, user = user1), getMentor(id = 2, user = user2))

        `when`(mentorService.readAll()).thenReturn(mentorsList)

        mockMvc.perform(
                get("/mentor/all")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(mentorsList)))

        verify(mentorService, times(1)).readAll()
    }

    @Test
    fun `Should fail while reading all mentors list because there aren't any`() {
        val errorMessage = MENTOR_LIST_IS_EMPTY

        `when`(mentorService.readAll()).thenAnswer { throw EmptyMentorListException(errorMessage) }

        mockMvc.perform(
                get("/mentor/all")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(mentorService, times(1)).readAll()
    }

    @Test
    fun `Should update a mentor successfully`() {
        val user = getUser(id = 1)
        val updateMentorRequest = getUpdateMentorRequest(userId = user.id!!)
        val mentor = getMentor(id = 1, user = user)

        `when`(mentorService.update(updateMentorRequest)).thenReturn(mentor)

        mockMvc.perform(
                patch("/mentor/update")
                    .accept(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(updateMentorRequest))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(mentor)))

        verify(mentorService, times(1)).update(updateMentorRequest)
    }

    @Test
    fun `Should fail while updating a mentor when the user is not a mentor`() {
        val user = getUser(id = 1)
        val updateMentorRequest = getUpdateMentorRequest(userId = user.id!!)
        val errorMessage = USER_IS_NOT_A_MENTOR + user.id!!

        `when`(mentorService.update(updateMentorRequest)).thenAnswer { throw UserIsNotAMentorException(errorMessage, user) }

        mockMvc.perform(
                patch("/mentor/update")
                    .accept(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(updateMentorRequest))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(mentorService, times(1)).update(updateMentorRequest)
    }

    @Test
    fun `Should fail while updating a mentor when the user doesn't exist`() {
        val user = getUser(id = 1)
        val updateMentorRequest = getUpdateMentorRequest(userId = user.id!!)
        val errorMessage = USER_NOT_FOUND_ID + user.id!!

        `when`(mentorService.update(updateMentorRequest)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
            patch("/mentor/update")
                .accept(APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(updateMentorRequest))
                .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(mentorService, times(1)).update(updateMentorRequest)
    }

    @Test
    fun `Should delete a mentor successfully`() {
        val user = getUser(id = 1)
        val deleteMentorResponse = DeleteMentorResponse(user, MENTOR_DELETED)

        `when`(mentorService.delete(user.id!!)).thenReturn(deleteMentorResponse)

        mockMvc.perform(
                delete("/mentor/delete/${user.id!!}")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(deleteMentorResponse)))

        verify(mentorService, times(1)).delete(user.id!!)
    }

    @Test
    fun `Should fail while deleting a mentor when the user that doesn't exist`() {
        val user = getUser(id = 1)
        val errorMessage = USER_NOT_FOUND_ID + user.id!!

        `when`(mentorService.delete(user.id!!)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
                delete("/mentor/delete/${user.id!!}")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(mentorService, times(1)).delete(user.id!!)
    }
}
