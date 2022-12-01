package com.devpath.repository

import com.devpath.entity.Mentor
import com.devpath.mock.MentorMockProvider.Companion.getPendingMentor
import com.devpath.mock.UserMockProvider.Companion.getUserWithoutId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode

@DataJpaTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class MentorRepositoryTests {
    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var mentorRepository: MentorRepository

    @Test
    fun `Mentor repository should be able to save a new mentor`() {
        val user = getUserWithoutId()
        entityManager.persistAndFlush(user)
        val mentor = getPendingMentor(id = 1, user = user)

        mentorRepository.saveAndFlush(mentor)

        val savedMentor = mentorRepository.findById(1).get()

        assertAttributes(mentor, savedMentor)
    }

    @Test
    fun `Mentor repository should be able to find a mentor by id`() {
        val user = getUserWithoutId()
        entityManager.persistAndFlush(user)
        val mentor = getPendingMentor(id = 1, user = user)
        entityManager.persistAndFlush(mentor)

        val foundedMentor = mentorRepository.findById(1).get()

        assertAttributes(mentor, foundedMentor)
    }

    @Test
    fun `Mentor repository should be able to find all mentors`() {
        val user1 = getUserWithoutId()
        entityManager.persistAndFlush(user1)
        val mentor1 = getPendingMentor(id = 1, user = user1)
        entityManager.persistAndFlush(mentor1)

        val user2 = getUserWithoutId()
        entityManager.persistAndFlush(user2)
        val mentor2 = getPendingMentor(id = 2, user = user2)
        entityManager.persistAndFlush(mentor2)

        val mentorsList = mentorRepository.findAll()

        assertThat(mentorsList)
            .hasSize(2)
            .contains(mentor1, mentor2)
    }

    @Test
    fun `Mentor repository should be able to find all mentors when the list is empty`() {
        val mentorsList = mentorRepository.findAll()
        assertThat(mentorsList.isEmpty())
    }

    @Test
    fun `Mentor repository should be able to find a mentor by user`() {
        val user = getUserWithoutId()
        entityManager.persistAndFlush(user)
        val mentor = getPendingMentor(id = 1, user = user)
        entityManager.persistAndFlush(mentor)

        val foundedMentor = mentorRepository.findByUser(user).get()

        assertAttributes(mentor, foundedMentor)
    }

    @Test
    fun `Mentor repository should be able to delete a mentor by id`() {
        val user = getUserWithoutId()
        entityManager.persistAndFlush(user)
        val mentor = getPendingMentor(id = 1, user = user)
        entityManager.persistAndFlush(mentor)

        mentorRepository.deleteById(mentor.id!!)

        assertThat(mentorRepository.findById(mentor.id!!).isEmpty)
    }

    private fun assertAttributes(expectedMentor: Mentor, actualMentor: Mentor) {
        assertNotNull(actualMentor.id)
        assertEquals(expectedMentor.user, actualMentor.user)
        assertEquals(expectedMentor.role, actualMentor.role)
        assertEquals(expectedMentor.yearsOfExperience, actualMentor.yearsOfExperience)
        assertEquals(expectedMentor.hourCost, actualMentor.hourCost)
        assertEquals(expectedMentor.payments, actualMentor.payments)
        assertEquals(expectedMentor.schedules, actualMentor.schedules)
    }
}
