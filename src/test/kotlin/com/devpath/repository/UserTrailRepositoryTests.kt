package com.devpath.repository

import com.devpath.mock.TrailMockProvider.Companion.getTrailWithoutId
import com.devpath.mock.UserTrailMockProvider.Companion.getUserTrailWithoutId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class UserTrailRepositoryTests {
    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var userTrailRepository: UserTrailRepository

    @Test
    fun `User trail repository should be able to delete an user trail`() {
        val trail = getTrailWithoutId()
        entityManager.persistAndFlush(trail)
        val userTrail = getUserTrailWithoutId()
        val savedUserTrail = entityManager.persistAndFlush(userTrail)

        userTrailRepository.delete(savedUserTrail)

        assertThat(userTrailRepository.findById(1).isEmpty)
    }
}
