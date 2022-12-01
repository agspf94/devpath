package com.devpath.service

import com.devpath.constants.Constants
import com.devpath.constants.Constants.Companion.MENTOR_DELETED
import com.devpath.constants.Constants.Companion.MENTOR_LIST_IS_EMPTY
import com.devpath.constants.Constants.Companion.MENTOR_STATUS_ACTIVE
import com.devpath.constants.Constants.Companion.USER_IS_NOT_A_MENTOR
import com.devpath.constants.Constants.Companion.USER_NOT_FOUND_ID
import com.devpath.entity.Mentor
import com.devpath.entity.User
import com.devpath.exception.exceptions.EmptyMentorListException
import com.devpath.exception.exceptions.UserDidntRequestToBecomeAMentorException
import com.devpath.exception.exceptions.UserIsNotAMentorException
import com.devpath.mock.MentorMockProvider.Companion.getApprovedMentor
import com.devpath.mock.MentorMockProvider.Companion.getUpdateMentorRequest
import com.devpath.mock.UserMockProvider.Companion.getMentorPendingUser
import com.devpath.mock.UserMockProvider.Companion.getMentorUser
import com.devpath.mock.UserMockProvider.Companion.getUser
import com.devpath.repository.MentorRepository
import com.devpath.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.any
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.Optional

@SpringBootTest
class MentorServiceTests {
    @MockBean
    private lateinit var mentorRepository: MentorRepository

    @MockBean
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var mentorService: MentorService

    @Test
    fun `Mentor service should not be null`() {
        assertNotNull(mentorService)
    }

    @Test
    fun `Become mentor should return a user with mentor status of pending`() {
        val user = getUser(id = 1)
        val mentorPendingUser = getMentorPendingUser(id = 1)

        `when`(userRepository.findById(user.id!!)).thenReturn(Optional.of(user))
        `when`(userRepository.saveAndFlush(any())).thenReturn(mentorPendingUser)

        val savedUser = mentorService.becomeMentor(user.id!!)

        assertAttributes(mentorPendingUser, savedUser)

        verify(userRepository, times(1)).findById(user.id!!)
        verify(userRepository, times(1)).saveAndFlush(any())
    }

    @Test
    fun `Become mentor should throw an exception when the user doesn't exist`() {
        val user = getUser(id = 1)

        `when`(userRepository.findById(user.id!!)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> { mentorService.becomeMentor(user.id!!) }
        assertEquals(USER_NOT_FOUND_ID + user.id, exception.message)

        verify(userRepository, times(1)).findById(user.id!!)
        verify(userRepository, times(0)).saveAndFlush(any())
    }

    @Test
    fun `Read should return the desired mentor when the user is a mentor`() {
        val user = getMentorUser(id = 1)
        val mentor = getApprovedMentor(id = 1, user = user)

        `when`(userRepository.findById(user.id!!)).thenReturn(Optional.of(user))
        `when`(mentorRepository.findById(user.id!!)).thenReturn(Optional.of(mentor))

        val savedMentor = mentorService.read(user.id!!)

        assertAttributes(mentor, savedMentor)

        verify(userRepository, times(1)).findById(user.id!!)
        verify(mentorRepository, times(1)).findById(mentor.id!!)
    }

    @Test
    fun `Read should throw an exception when the user is not a mentor`() {
        val user = getUser(id = 1)

        `when`(userRepository.findById(user.id!!)).thenReturn(Optional.of(user))

        val exception = assertThrows<UserIsNotAMentorException> { mentorService.read(user.id!!) }
        assertEquals(user, exception.user)
        assertEquals(USER_IS_NOT_A_MENTOR, exception.message)

        verify(userRepository, times(1)).findById(user.id!!)
        verify(mentorRepository, times(0)).findById(any())
    }

    @Test
    fun `Read should throw an exception when the user doesn't exist`() {
        val user = getUser(id = 1)

        `when`(userRepository.findById(user.id!!)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> { mentorService.read(user.id!!) }
        assertEquals(USER_NOT_FOUND_ID + user.id!!, exception.message)

        verify(userRepository, times(1)).findById(user.id!!)
        verify(mentorRepository, times(0)).findById(any())
    }

    @Test
    fun `Read all should return all mentors list`() {
        val user1 = getMentorUser(id = 1)
        val mentor1 = getApprovedMentor(id = 1, user = user1)
        val user2 = getMentorUser(id = 1)
        val mentor2 = getApprovedMentor(id = 2, user = user2)

        `when`(mentorRepository.findAll()).thenReturn(listOf(mentor1, mentor2))

        val mentorsList = mentorService.readAll()

        assertThat(mentorsList)
            .hasSize(2)
            .contains(mentor1, mentor2)

        verify(mentorRepository, times(1)).findAll()
    }

    @Test
    fun `Read all should throw an exception when all mentors list is empty`() {
        `when`(mentorRepository.findAll()).thenReturn(listOf())

        val exception = assertThrows<EmptyMentorListException> { mentorService.readAll() }
        assertEquals(MENTOR_LIST_IS_EMPTY, exception.message)

        verify(mentorRepository, times(1)).findAll()
    }

    @Test
    fun `Update should update the mentor and return it updated`() {
        val updateMentorRequest = getUpdateMentorRequest(userId = 1)
        val user = getUser(id = 1)
        val mentor = getApprovedMentor(id = 1, user = user)

        `when`(userRepository.findById(updateMentorRequest.userId)).thenReturn(Optional.of(user))
        `when`(mentorRepository.findById(user.id!!)).thenReturn(Optional.of(mentor))
        `when`(mentorRepository.saveAndFlush(any())).thenReturn(mentor)

        val updatedMentor = mentorService.update(updateMentorRequest)

        assertEquals(updateMentorRequest.role, updatedMentor.role)
        assertEquals(updateMentorRequest.yearsOfExperience, updatedMentor.yearsOfExperience)
        assertEquals(updateMentorRequest.hourCost, updatedMentor.hourCost)

        verify(userRepository, times(1)).findById(updateMentorRequest.userId)
        verify(mentorRepository, times(1)).findById(mentor.id!!)
        verify(mentorRepository, times(1)).saveAndFlush(any())
    }

    @Test
    fun `Update should throw an exception when the user is not a mentor`() {
        val updateMentorRequest = getUpdateMentorRequest(userId = 1)
        val user = getUser(id = 1)

        `when`(userRepository.findById(updateMentorRequest.userId)).thenReturn(Optional.of(user))

        val exception = assertThrows<UserIsNotAMentorException> { mentorService.update(updateMentorRequest) }
        assertEquals(USER_IS_NOT_A_MENTOR, exception.message)
        assertEquals(user, exception.user)

        verify(userRepository, times(1)).findById(user.id!!)
        verify(mentorRepository, times(0)).findById(any())
        verify(mentorRepository, times(0)).saveAndFlush(any())
    }

    @Test
    fun `Update should throw an exception when the user doesn't exist`() {
        val updateMentorRequest = getUpdateMentorRequest(userId = 1)
        val user = getUser(id = 1)

        `when`(userRepository.findById(updateMentorRequest.userId)).thenReturn(Optional.of(user))

        val exception = assertThrows<UserIsNotAMentorException> { mentorService.update(updateMentorRequest) }
        assertEquals(USER_IS_NOT_A_MENTOR, exception.message)
        assertEquals(user, exception.user)

        verify(userRepository, times(1)).findById(user.id!!)
        verify(mentorRepository, times(0)).findById(any())
        verify(mentorRepository, times(0)).saveAndFlush(any())
    }

    @Test
    fun `Delete should make the user not a mentor anymore`() {
        val mentorUser = getMentorUser(id = 1)
        val mentor = getApprovedMentor(id = 1, user = mentorUser)
        val user = getUser(id = 1)

        `when`(userRepository.findById(mentorUser.id!!)).thenReturn(Optional.of(mentorUser))
        `when`(mentorRepository.findByUser(mentorUser)).thenReturn(Optional.of(mentor))
        doNothing().`when`(mentorRepository).deleteById(mentor.id!!)
        `when`(userRepository.saveAndFlush(any())).thenReturn(mentorUser)

        val deleteMentorResponse = mentorService.delete(mentorUser.id!!)

        assertEquals(user, deleteMentorResponse.user)
        assertEquals(MENTOR_DELETED, deleteMentorResponse.message)

        verify(userRepository, times(1)).findById(mentorUser.id!!)
        verify(mentorRepository, times(1)).findByUser(mentorUser)
        verify(mentorRepository, times(1)).deleteById(mentor.id!!)
        verify(userRepository, times(1)).saveAndFlush(any())
    }

    @Test
    fun `Delete should throw an exception when the user is not a mentor`() {
        val user = getUser(id = 1)

        `when`(userRepository.findById(user.id!!)).thenReturn(Optional.of(user))

        val exception = assertThrows<UserIsNotAMentorException> { mentorService.delete(user.id!!) }
        assertEquals(USER_IS_NOT_A_MENTOR, exception.message)
        assertEquals(user, exception.user)

        verify(userRepository, times(1)).findById(user.id!!)
        verify(mentorRepository, times(0)).findByUser(user)
        verify(mentorRepository, times(0)).deleteById(any())
        verify(userRepository, times(0)).saveAndFlush(any())
    }

    @Test
    fun `Delete should throw an exception when the user doesn't exist`() {
        val user = getUser(id = 1)

        `when`(userRepository.findById(user.id!!)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> { mentorService.delete(user.id!!) }
        assertEquals(USER_NOT_FOUND_ID + user.id!!, exception.message)

        verify(userRepository, times(1)).findById(user.id!!)
        verify(mentorRepository, times(0)).findByUser(user)
        verify(mentorRepository, times(0)).deleteById(any())
        verify(userRepository, times(0)).saveAndFlush(any())
    }

    @Test
    fun `Approve mentor should return the approved mentor`() {
        val user = getMentorPendingUser(id = 1)
        val mentorUser = getMentorUser(id = 1)
        val mentor = getApprovedMentor(id = 1, getMentorPendingUser(id = 1))

        `when`(userRepository.findById(user.id!!)).thenReturn(Optional.of(user))
        `when`(userRepository.saveAndFlush(any())).thenReturn(mentorUser)
        `when`(mentorRepository.saveAndFlush(any())).thenReturn(any())

        val approvedMentor = mentorService.approveMentor(user.id!!)

        assertAttributes(mentor, approvedMentor)

        verify(userRepository, times(1)).findById(user.id!!)
        verify(userRepository, times(1)).saveAndFlush(any())
        verify(mentorRepository, times(1)).saveAndFlush(any())
    }

    @Test
    fun `Approve mentor should throw an exception when the user didn't request to become a mentor`() {
        val user = getUser(id = 1)

        `when`(userRepository.findById(user.id!!)).thenReturn(Optional.of(user))

        val exception = assertThrows<UserDidntRequestToBecomeAMentorException> { mentorService.approveMentor(user.id!!) }
        assertEquals(Constants.USER_DIDNT_REQUEST_TO_BECOME_A_MENTOR, exception.message)

        verify(userRepository, times(1)).findById(user.id!!)
        verify(userRepository, times(0)).saveAndFlush(any())
        verify(mentorRepository, times(0)).saveAndFlush(any())
    }

    @Test
    fun `Approve mentor should throw an exception when the user doesn't exist`() {
        val user = getUser(id = 1)

        `when`(userRepository.findById(user.id!!)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> { mentorService.approveMentor(user.id!!) }
        assertEquals(USER_NOT_FOUND_ID + user.id, exception.message)

        verify(userRepository, times(1)).findById(user.id!!)
        verify(userRepository, times(0)).saveAndFlush(any())
        verify(mentorRepository, times(0)).saveAndFlush(any())
    }

    private fun assertAttributes(expectedUser: User, savedUser: User) {
        assertNotNull(expectedUser.id)
        assertEquals(expectedUser.name, savedUser.name)
        assertEquals(expectedUser.email, savedUser.email)
        assertEquals(expectedUser.password, savedUser.password)
        assertEquals(expectedUser.mentorStatus, savedUser.mentorStatus)
        assertEquals(expectedUser.userTrails, savedUser.userTrails)
    }

    private fun assertAttributes(expectedMentor: Mentor, savedMentor: Mentor) {
        assertNotNull(expectedMentor.id)
        assertEquals(MENTOR_STATUS_ACTIVE, savedMentor.user.mentorStatus)
        assertEquals(expectedMentor.user, savedMentor.user)
        assertEquals(expectedMentor.role, savedMentor.role)
        assertEquals(expectedMentor.yearsOfExperience, savedMentor.yearsOfExperience)
        assertEquals(expectedMentor.hourCost, savedMentor.hourCost)
        assertEquals(expectedMentor.payments, savedMentor.payments)
        assertEquals(expectedMentor.schedules, savedMentor.schedules)
    }
}
