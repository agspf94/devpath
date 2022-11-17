package com.devpath.controller

import com.devpath.constants.Constants.Companion.TOPIC_ALREADY_EXISTS
import com.devpath.constants.Constants.Companion.TOPIC_DELETED
import com.devpath.constants.Constants.Companion.TOPIC_LIST_IS_EMPTY
import com.devpath.constants.Constants.Companion.TOPIC_NOT_FOUND_ID
import com.devpath.constants.Constants.Companion.TOPIC_NOT_FOUND_NAME
import com.devpath.dto.topic.response.DeleteTopicResponse
import com.devpath.exception.ErrorMessage
import com.devpath.exception.exceptions.EmptyTopicListException
import com.devpath.exception.exceptions.TopicAlreadyExistsException
import com.devpath.mock.TopicMockProvider.Companion.getCreateTopicRequest
import com.devpath.mock.TopicMockProvider.Companion.getTopic
import com.devpath.mock.TopicMockProvider.Companion.getUpdateTopicRequest
import com.devpath.service.TopicService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(TopicController::class)
class TopicControllerTest {
    @MockBean
    private lateinit var topicService: TopicService

    @Autowired
    private lateinit var topicController: TopicController

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `Topic controller should not be null`() {
        assertNotNull(topicController)
    }

    @Test
    fun `Should create a topic successfully`() {
        val createTopicRequest = getCreateTopicRequest()
        val topic = getTopic(id = 1)

        `when`(topicService.create(createTopicRequest)).thenReturn(topic)

        mockMvc.perform(
                post("/topic/create")
                    .accept(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(createTopicRequest))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(topic)))

        verify(topicService, times(1)).create(createTopicRequest)
    }

    @Test
    fun `Should fail while creating a topic with a name that already exists`() {
        val createTopicRequest = getCreateTopicRequest()
        val errorMessage = TOPIC_ALREADY_EXISTS + createTopicRequest.name

        `when`(topicService.create(createTopicRequest)).thenAnswer { throw TopicAlreadyExistsException(errorMessage) }

        mockMvc.perform(
                post("/topic/create")
                    .accept(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(createTopicRequest))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(topicService, times(1)).create(createTopicRequest)
    }

    @Test
    fun `Should read a topic successfully`() {
        val topic = getTopic(id = 1)

        `when`(topicService.read(topic.id!!)).thenReturn(topic)

        mockMvc.perform(
                get("/topic/${topic.id!!}")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(topic)))

        verify(topicService, times(1)).read(topic.id!!)
    }

    @Test
    fun `Should fail while reading a topic that doesn't exist`() {
        val topic = getTopic(id = 1)
        val errorMessage = TOPIC_NOT_FOUND_ID + topic.id

        `when`(topicService.read(topic.id!!)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
            get("/topic/${topic.id!!}")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(topicService, times(1)).read(topic.id!!)
    }

    @Test
    fun `Should read all topics list successfully`() {
        val topicsList = listOf(getTopic(id = 1), getTopic(id = 2))

        `when`(topicService.readAll()).thenReturn(topicsList)

        mockMvc.perform(
                get("/topic/all")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(topicsList)))

        verify(topicService, times(1)).readAll()
    }

    @Test
    fun `Should fail while reading all topics list because there aren't any`() {
        val errorMessage = TOPIC_LIST_IS_EMPTY

        `when`(topicService.readAll()).thenAnswer { throw EmptyTopicListException(errorMessage) }

        mockMvc.perform(
                get("/topic/all")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(topicService, times(1)).readAll()
    }

    @Test
    fun `Should update a topic successfully`() {
        val updateTopicRequest = getUpdateTopicRequest(id = 1)
        val topic = getTopic(id = 1)

        `when`(topicService.update(updateTopicRequest)).thenReturn(topic)

        mockMvc.perform(
                patch("/topic/update")
                    .accept(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(updateTopicRequest))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(topic)))

        verify(topicService, times(1)).update(updateTopicRequest)
    }

    @Test
    fun `Should fail while updating a topic that doesn't exist`() {
        val updateTopicRequest = getUpdateTopicRequest(id = 1)
        val errorMessage = TOPIC_NOT_FOUND_NAME + updateTopicRequest.name

        `when`(topicService.update(updateTopicRequest)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
                patch("/topic/update")
                    .accept(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(updateTopicRequest))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(topicService, times(1)).update(updateTopicRequest)
    }

    @Test
    fun `Should delete a topic successfully`() {
        val topic = getTopic(id = 1)
        val deleteTopicResponse = DeleteTopicResponse(topic, TOPIC_DELETED)

        `when`(topicService.delete(topic.id!!)).thenReturn(deleteTopicResponse)

        mockMvc.perform(
                delete("/topic/delete/${topic.id!!}")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(deleteTopicResponse)))

        verify(topicService, times(1)).delete(topic.id!!)
    }

    @Test
    fun `Should fail while deleting a topic that doesn't exist`() {
        val topic = getTopic(id = 1)
        val errorMessage = TOPIC_NOT_FOUND_ID + topic.id!!

        `when`(topicService.delete(topic.id!!)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
                delete("/topic/delete/${topic.id!!}")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(topicService, times(1)).delete(topic.id!!)
    }
}
