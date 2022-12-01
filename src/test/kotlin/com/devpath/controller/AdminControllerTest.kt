package com.devpath.controller

import com.devpath.constants.Constants
import com.devpath.constants.Constants.Companion.USER_NOT_FOUND_ID
import com.devpath.exception.ErrorMessage
import com.devpath.exception.exceptions.UserDidntRequestToBecomeAMentorException
import com.devpath.mock.MentorMockProvider.Companion.getApprovedMentor
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(AdminController::class)
class AdminControllerTest {
    @MockBean
    private lateinit var mentorService: MentorService

    @Autowired
    private lateinit var adminController: AdminController

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `Admin controller should not be null`() {
        assertNotNull(adminController)
    }

    @Test
    fun `Approve mentor should approve a mentor successfully`() {
        val user = getUser(id = 1)
        val mentor = getApprovedMentor(id = 1, user = user)

        `when`(mentorService.approveMentor(user.id!!)).thenReturn(mentor)

        mockMvc.perform(
            post("/admin/approve-mentor/${user.id!!}")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(mentor)))

        verify(mentorService, times(1)).approveMentor(user.id!!)
    }

    @Test
    fun `Approve mentor should fail when the user doesn't exist`() {
        val userId = 1
        val errorMessage = USER_NOT_FOUND_ID + userId

        `when`(mentorService.approveMentor(userId)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
            post("/admin/approve-mentor/$userId")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(mentorService, times(1)).approveMentor(userId)
    }

    @Test
    fun `Approve mentor should fail when the user didn't request to become a mentor`() {
        val userId = 1
        val errorMessage = Constants.USER_DIDNT_REQUEST_TO_BECOME_A_MENTOR

        `when`(mentorService.approveMentor(userId)).thenAnswer { throw UserDidntRequestToBecomeAMentorException(errorMessage) }

        mockMvc.perform(
            post("/admin/approve-mentor/$userId")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(mentorService, times(1)).approveMentor(userId)
    }
}
