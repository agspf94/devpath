package com.devpath.repository

import com.devpath.entity.SubTopic
import com.devpath.mock.SubTopicMockProvider.Companion.getSubTopicWithoutId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class SubTopicRepositoryTests {
    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var subTopicRepository: SubTopicRepository

    @Test
    fun `Sub topic repository should be able to find a sub topic by name`() {
        val subTopic = getSubTopicWithoutId()
        entityManager.persistAndFlush(subTopic)

        val foundedSubTopic = subTopicRepository.findByName(subTopic.name).get()

        assertAttributes(subTopic, foundedSubTopic)
    }

    @Test
    fun `Sub topic repository should be able to save a new sub topic`() {
        val subTopic = getSubTopicWithoutId()

        subTopicRepository.saveAndFlush(subTopic)

        val savedSubTopic = subTopicRepository.findById(1).get()

        assertAttributes(subTopic, savedSubTopic)
    }

    @Test
    fun `Sub topic repository should be able to find a sub topic by id`() {
        val subTopic = getSubTopicWithoutId()
        entityManager.persistAndFlush(subTopic)

        val foundedSubTopic = subTopicRepository.findById(1).get()

        assertAttributes(subTopic, foundedSubTopic)
    }

    @Test
    fun `Sub topic repository should be able to find all sub topics`() {
        val subTopic1 = getSubTopicWithoutId()
        entityManager.persistAndFlush(subTopic1)
        val subTopic2 = getSubTopicWithoutId()
        entityManager.persistAndFlush(subTopic2)

        val subTopicsList = subTopicRepository.findAll()

        assertThat(subTopicsList)
            .hasSize(2)
            .contains(subTopic1, subTopic2)
    }

    @Test
    fun `Sub topic repository should be able to find all sub topics when the list is empty`() {
        val subTopicsList = subTopicRepository.findAll()
        assertThat(subTopicsList.isEmpty())
    }

    @Test
    fun `Sub topic repository should be able to delete a sub topic by id`() {
        val subTopic = getSubTopicWithoutId()
        entityManager.persistAndFlush(subTopic)

        subTopicRepository.deleteById(1)

        assertThat(subTopicRepository.findById(1).isEmpty)
    }

    private fun assertAttributes(expectedSubTopic: SubTopic, actualSubTopic: SubTopic) {
        assertNotNull(expectedSubTopic.id)
        assertEquals(actualSubTopic.name, expectedSubTopic.name)
        assertEquals(actualSubTopic.content, expectedSubTopic.content)
    }
}
