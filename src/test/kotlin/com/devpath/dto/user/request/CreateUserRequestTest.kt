package com.devpath.dto.user.request

import com.devpath.entity.UserTrail
import com.devpath.mock.UserMockProvider.Companion.getCreateUserRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CreateUserRequestTest {
    @Test
    fun `Create user request should be able to provide a user from itself`() {
        val createUserRequest = getCreateUserRequest()
        val user = createUserRequest.toUser()

        assertEquals(createUserRequest.name, user.name)
        assertEquals(createUserRequest.email, user.email)
        assertEquals(createUserRequest.password, user.password)
        assertEquals(emptySet<UserTrail>(), user.userTrails)
    }
}
