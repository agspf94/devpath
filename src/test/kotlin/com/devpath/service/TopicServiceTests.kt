package com.devpath.service

import com.devpath.constants.Constants.Companion.TOPIC_ALREADY_EXISTS
import com.devpath.constants.Constants.Companion.TOPIC_DELETED
import com.devpath.constants.Constants.Companion.TOPIC_LIST_IS_EMPTY
import com.devpath.constants.Constants.Companion.TOPIC_NOT_FOUND_ID
import com.devpath.constants.Constants.Companion.TOPIC_NOT_FOUND_NAME
import com.devpath.entity.Topic
import com.devpath.exception.exceptions.topic.EmptyTopicListException
import com.devpath.exception.exceptions.topic.TopicAlreadyExistsException
import com.devpath.mock.TopicMockProvider.Companion.getCreateTopicRequest
import com.devpath.mock.TopicMockProvider.Companion.getTopic
import com.devpath.mock.TopicMockProvider.Companion.getUpdateTopicRequest
import com.devpath.repository.TopicRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.Optional

@SpringBootTest
class TopicServiceTests {
    @MockBean
    private lateinit var topicRepository: TopicRepository

    @Autowired
    private lateinit var topicService: TopicService

    @Test
    fun `Topic service should not be null`() {
        assertNotNull(topicService)
    }

    @Test
    fun `Create topic should return the new topic`() {
        val createTopicRequest = getCreateTopicRequest()
        val topic = getTopic(id = 1)

        `when`(topicRepository.findByName(createTopicRequest.name)).thenReturn(Optional.empty())
        `when`(topicRepository.saveAndFlush(any())).thenReturn(topic)

        val savedTopic = topicService.create(createTopicRequest)

        assertAttributes(topic, savedTopic)

        verify(topicRepository, times(1)).findByName(createTopicRequest.name)
        verify(topicRepository, times(1)).saveAndFlush(any())
    }

    @Test
    fun `Create topic should throw an exception when the topic already exists`() {
        val createTopicRequest = getCreateTopicRequest()
        val topic = getTopic(id = 1)

        `when`(topicRepository.findByName(createTopicRequest.name)).thenReturn(Optional.of(topic))

        val exception = assertThrows<TopicAlreadyExistsException> { topicService.create(createTopicRequest) }
        assertEquals(TOPIC_ALREADY_EXISTS + createTopicRequest.name, exception.message)

        verify(topicRepository, times(1)).findByName(createTopicRequest.name)
        verify(topicRepository, times(0)).saveAndFlush(any())
    }

    @Test
    fun `Read should return the desired topic`() {
        val topic = getTopic(id = 1)

        `when`(topicRepository.findById(topic.id!!)).thenReturn(Optional.of(topic))

        val savedTopic = topicService.read(topic.id!!)

        assertAttributes(topic, savedTopic)

        verify(topicRepository, times(1)).findById(topic.id!!)
    }

    @Test
    fun `Read should throw an exception when the topic doesn't exist`() {
        val topic = getTopic(id = 1)

        `when`(topicRepository.findById(topic.id!!)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> { topicService.read(topic.id!!) }
        assertEquals(TOPIC_NOT_FOUND_ID + topic.id, exception.message)

        verify(topicRepository, times(1)).findById(topic.id!!)
    }

    @Test
    fun `Read all should return all topics list`() {
        val topic1 = getTopic(id = 1)
        val topic2 = getTopic(id = 2)

        `when`(topicRepository.findAll()).thenReturn(listOf(topic1, topic2))

        val topicsList = topicService.readAll()

        assertThat(topicsList)
            .hasSize(2)
            .contains(topic1, topic2)

        verify(topicRepository, times(1)).findAll()
    }

    @Test
    fun `Read all should throw an exception when all topics list is empty`() {
        `when`(topicRepository.findAll()).thenReturn(listOf())

        val exception = assertThrows<EmptyTopicListException> { topicService.readAll() }
        assertEquals(TOPIC_LIST_IS_EMPTY, exception.message)

        verify(topicRepository, times(1)).findAll()
    }

    @Test
    fun `Update should update the topic and return it updated`() {
        val updateTopicRequest = getUpdateTopicRequest(id = 1)
        val topic = getTopic(id = 1)

        `when`(topicRepository.findById(updateTopicRequest.id)).thenReturn(Optional.of(topic))
        `when`(topicRepository.saveAndFlush(any())).thenReturn(topic)

        val updatedTopic = topicService.update(updateTopicRequest)

        assertEquals(updateTopicRequest.name, updatedTopic.name)

        verify(topicRepository, times(1)).findById(updateTopicRequest.id)
        verify(topicRepository, times(1)).saveAndFlush(any())
    }

    @Test
    fun `Update should throw an exception when the topic doesn't exist`() {
        val updateTopicRequest = getUpdateTopicRequest(id = 1)

        `when`(topicRepository.findById(updateTopicRequest.id)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> { topicService.update(updateTopicRequest) }
        assertEquals(TOPIC_NOT_FOUND_NAME + updateTopicRequest.name, exception.message)

        verify(topicRepository, times(1)).findById(updateTopicRequest.id)
        verify(topicRepository, times(0)).saveAndFlush(any())
    }

    @Test
    fun `Delete should delete the desired topic`() {
        val topic = getTopic(id = 1)

        `when`(topicRepository.findById(topic.id!!)).thenReturn(Optional.of(topic))
        doNothing().`when`(topicRepository).deleteById(topic.id!!)

        val deleteTopicResponse = topicService.delete(topic.id!!)

        assertEquals(topic, deleteTopicResponse.topic)
        assertEquals(TOPIC_DELETED, deleteTopicResponse.message)

        verify(topicRepository, times(1)).findById(topic.id!!)
        verify(topicRepository, times(1)).deleteById(topic.id!!)
    }

    @Test
    fun `Delete should throw an exception when the topic doesn't exist`() {
        val topic = getTopic(id = 1)

        `when`(topicRepository.findById(topic.id!!)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> { topicService.delete(topic.id!!) }
        assertEquals(TOPIC_NOT_FOUND_ID + topic.id!!, exception.message)

        verify(topicRepository, times(1)).findById(topic.id!!)
        verify(topicRepository, times(0)).deleteById(any())
    }

    private fun assertAttributes(expectedTopic: Topic, savedTopic: Topic) {
        assertNotNull(expectedTopic.id)
        assertEquals(expectedTopic.name, savedTopic.name)
        assertEquals(expectedTopic.subTopics, savedTopic.subTopics)
    }
}
