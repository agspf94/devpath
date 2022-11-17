package com.devpath.entity

import com.devpath.dto.trail.TrailDTO
import com.devpath.mock.UserMockProvider.Companion.getUser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserTests {
    @Test
    fun `User should be able to provide an user DTO from itself`() {
        val user = getUser(id = 1)
        val userDTO = user.toUserDTO()

        assertEquals(user.id, userDTO.id)
        assertEquals(user.name, userDTO.name)
        assertEquals(user.email, userDTO.email)
        assertEquals(user.password, userDTO.password)
        assertEquals(user.isMentor, userDTO.isMentor)
        assertEquals(emptySet<TrailDTO>(), userDTO.trails)
    }
}
