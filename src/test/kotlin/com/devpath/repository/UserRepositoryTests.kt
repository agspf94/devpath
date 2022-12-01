package com.devpath.repository

import com.devpath.entity.User
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
class UserRepositoryTests {
    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `User repository should be able to find an user by email`() {
        val user = getUserWithoutId()
        entityManager.persistAndFlush(user)

        val foundedUser = userRepository.findByEmail(user.email).get()

        assertAttributes(user, foundedUser)
    }

    @Test
    fun `User repository should be able to save a new user`() {
        val user = getUserWithoutId()

        userRepository.saveAndFlush(user)

        val savedUser = userRepository.findById(1).get()

        assertAttributes(user, savedUser)
    }

    @Test
    fun `User repository should be able to find an user by id`() {
        val user = getUserWithoutId()
        entityManager.persistAndFlush(user)

        val foundedUser = userRepository.findById(1).get()

        assertAttributes(user, foundedUser)
    }

    @Test
    fun `User repository should be able to delete an user by id`() {
        val user = getUserWithoutId()
        entityManager.persistAndFlush(user)

        userRepository.deleteById(user.id!!)

        assertThat(userRepository.findById(user.id!!).isEmpty)
    }

    private fun assertAttributes(expectedUser: User, actualUser: User) {
        assertNotNull(expectedUser.id)
        assertEquals(actualUser.name, expectedUser.name)
        assertEquals(actualUser.email, expectedUser.email)
        assertEquals(actualUser.password, expectedUser.password)
        assertEquals(actualUser.mentorStatus, expectedUser.mentorStatus)
        assertEquals(actualUser.userTrails, expectedUser.userTrails)
    }
}
