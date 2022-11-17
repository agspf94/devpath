package com.devpath.repository

import com.devpath.entity.Topic
import com.devpath.mock.TopicMockProvider.Companion.getTopicWithoutId
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
class TopicRepositoryTests {
    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var topicRepository: TopicRepository

    @Test
    fun `Topic repository should be able to find a topic by name`() {
        val topic = getTopicWithoutId()
        entityManager.persistAndFlush(topic)

        val foundedTopic = topicRepository.findByName(topic.name).get()

        assertAttributes(topic, foundedTopic)
    }

    @Test
    fun `Topic repository should be able to save a new topic`() {
        val topic = getTopicWithoutId()

        topicRepository.saveAndFlush(topic)

        val savedTopic = topicRepository.findById(1).get()

        assertAttributes(topic, savedTopic)
    }

    @Test
    fun `Topic repository should be able to find a topic by id`() {
        val topic = getTopicWithoutId()
        entityManager.persistAndFlush(topic)

        val foundedTopic = topicRepository.findById(1).get()

        assertAttributes(topic, foundedTopic)
    }

    @Test
    fun `Topic repository should be able to find all topics`() {
        val topic1 = getTopicWithoutId()
        entityManager.persistAndFlush(topic1)
        val topic2 = getTopicWithoutId()
        entityManager.persistAndFlush(topic2)

        val topicsList = topicRepository.findAll()

        assertThat(topicsList)
            .hasSize(2)
            .contains(topic1, topic2)
    }

    @Test
    fun `Topic repository should be able to find all topics when the list is empty`() {
        val topicsList = topicRepository.findAll()
        assertThat(topicsList.isEmpty())
    }

    @Test
    fun `Topic repository should be able to delete a topic by id`() {
        val topic = getTopicWithoutId()
        entityManager.persistAndFlush(topic)

        topicRepository.deleteById(1)

        assertThat(topicRepository.findById(1).isEmpty)
    }

    private fun assertAttributes(expectedTopic: Topic, actualTopic: Topic) {
        assertNotNull(expectedTopic.id)
        assertEquals(actualTopic.name, expectedTopic.name)
        assertEquals(actualTopic.subTopics, expectedTopic.subTopics)
    }
}
