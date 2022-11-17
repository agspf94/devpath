package com.devpath.repository

import com.devpath.entity.Trail
import com.devpath.mock.TrailMockProvider.Companion.getTrailWithoutId
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
class TrailRepositoryTests {
    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var trailRepository: TrailRepository

    @Test
    fun `Trail repository should be able to find a trail by name`() {
        val trail = getTrailWithoutId()
        entityManager.persistAndFlush(trail)

        val foundedTrail = trailRepository.findByName(trail.name).get()

        assertAttributes(trail, foundedTrail)
    }

    @Test
    fun `Trail repository should be able to save a new trail`() {
        val trail = getTrailWithoutId()

        trailRepository.saveAndFlush(trail)

        val savedTrail = trailRepository.findById(1).get()

        assertAttributes(trail, savedTrail)
    }

    @Test
    fun `Trail repository should be able to find a trail by id`() {
        val trail = getTrailWithoutId()
        entityManager.persistAndFlush(trail)

        val foundedTrail = trailRepository.findById(1).get()

        assertAttributes(trail, foundedTrail)
    }

    @Test
    fun `Trail repository should be able to find all trails`() {
        val trail1 = getTrailWithoutId()
        entityManager.persistAndFlush(trail1)
        val trail2 = getTrailWithoutId()
        entityManager.persistAndFlush(trail2)

        val trailsList = trailRepository.findAll()

        assertThat(trailsList)
            .hasSize(2)
            .contains(trail1, trail2)
    }

    @Test
    fun `Trail repository should be able to find all trails when the list is empty`() {
        val trailsList = trailRepository.findAll()
        assertThat(trailsList.isEmpty())
    }

    @Test
    fun `Trail repository should be able to delete a trail by id`() {
        val trail = getTrailWithoutId()
        entityManager.persistAndFlush(trail)

        trailRepository.deleteById(1)

        assertThat(trailRepository.findById(1).isEmpty)
    }

    private fun assertAttributes(expectedTrail: Trail, actualTrail: Trail) {
        assertNotNull(expectedTrail.id)
        assertEquals(actualTrail.name, expectedTrail.name)
        assertEquals(actualTrail.duration, expectedTrail.duration)
        assertEquals(actualTrail.description, expectedTrail.description)
        assertEquals(actualTrail.averageSalary, expectedTrail.averageSalary)
        assertEquals(actualTrail.jobs, expectedTrail.jobs)
        assertEquals(actualTrail.topics, expectedTrail.topics)
    }
}
