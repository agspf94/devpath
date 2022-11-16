package com.devpath.controller

import com.devpath.constants.Constants
import com.devpath.constants.Constants.Companion.SUB_TOPIC_DELETED
import com.devpath.constants.Constants.Companion.SUB_TOPIC_NOT_FOUND
import com.devpath.dto.subtopic.response.DeleteSubTopicResponse
import com.devpath.exception.ErrorMessage
import com.devpath.exception.exceptions.EmptySubTopicListException
import com.devpath.exception.exceptions.SubTopicAlreadyExistsException
import com.devpath.mocks.SubTopicMockProvider.Companion.getCreateSubTopicRequest
import com.devpath.mocks.SubTopicMockProvider.Companion.getSubTopic
import com.devpath.mocks.SubTopicMockProvider.Companion.getUpdateSubTopicRequest
import com.devpath.service.SubTopicService
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

@WebMvcTest(SubTopicController::class)
class SubTopicControllerTest {
    @MockBean
    private lateinit var subTopicService: SubTopicService

    @Autowired
    private lateinit var subTopicController: SubTopicController

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `SubTopic controller should not be null`() {
        assertNotNull(subTopicController)
    }

    @Test
    fun `Should create a sub topic successfully`() {
        val createSubTopicRequest = getCreateSubTopicRequest()
        val subTopic = getSubTopic(id = 1)

        `when`(subTopicService.create(createSubTopicRequest)).thenReturn(subTopic)

        mockMvc.perform(
                post("/sub-topic/create")
                    .accept(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(createSubTopicRequest))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(subTopic)))

        verify(subTopicService, times(1)).create(createSubTopicRequest)
    }

    @Test
    fun `Should fails while creating a sub topic with a name that already exists`() {
        val createSubTopicRequest = getCreateSubTopicRequest()
        val errorMessage = Constants.SUB_TOPIC_ALREADY_EXISTS + createSubTopicRequest.name

        `when`(subTopicService.create(createSubTopicRequest)).thenAnswer { throw SubTopicAlreadyExistsException(errorMessage) }

        mockMvc.perform(
                post("/sub-topic/create")
                    .accept(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(createSubTopicRequest))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(subTopicService, times(1)).create(createSubTopicRequest)
    }

    @Test
    fun `Should read a sub topic successfully`() {
        val subTopic = getSubTopic(id = 1)

        `when`(subTopicService.read(subTopic.id!!)).thenReturn(subTopic)

        mockMvc.perform(
                get("/sub-topic/${subTopic.id!!}")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(subTopic)))

        verify(subTopicService, times(1)).read(subTopic.id!!)
    }

    @Test
    fun `Should fails while reading a sub topic that doesn't exist`() {
        val subTopic = getSubTopic(id = 1)
        val errorMessage = SUB_TOPIC_NOT_FOUND + subTopic.id

        `when`(subTopicService.read(subTopic.id!!)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
            get("/sub-topic/${subTopic.id!!}")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(subTopicService, times(1)).read(subTopic.id!!)
    }

    @Test
    fun `Should read all sub topics list successfully`() {
        val subTopicsList = listOf(getSubTopic(id = 1), getSubTopic(id = 2))

        `when`(subTopicService.readAll()).thenReturn(subTopicsList)

        mockMvc.perform(
                get("/sub-topic/all")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(subTopicsList)))

        verify(subTopicService, times(1)).readAll()
    }

    @Test
    fun `Should fails while reading all sub topics list because there aren't any`() {
        val errorMessage = Constants.SUB_TOPIC_LIST_IS_EMPTY

        `when`(subTopicService.readAll()).thenAnswer { throw EmptySubTopicListException(errorMessage) }

        mockMvc.perform(
                get("/sub-topic/all")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(subTopicService, times(1)).readAll()
    }

    @Test
    fun `Should update a sub topic successfully`() {
        val updateSubTopicRequest = getUpdateSubTopicRequest(id = 1)
        val subTopic = getSubTopic(id = 1)

        `when`(subTopicService.update(updateSubTopicRequest)).thenReturn(subTopic)

        mockMvc.perform(
                patch("/sub-topic/update")
                    .accept(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(updateSubTopicRequest))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(subTopic)))

        verify(subTopicService, times(1)).update(updateSubTopicRequest)
    }

    @Test
    fun `Should fails while updating a sub topic that doesn't exist`() {
        val updateSubTopicRequest = getUpdateSubTopicRequest(id = 1)
        val errorMessage = SUB_TOPIC_NOT_FOUND + updateSubTopicRequest.id

        `when`(subTopicService.update(updateSubTopicRequest)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
                patch("/sub-topic/update")
                    .accept(APPLICATION_JSON)
                    .content(jacksonObjectMapper().writeValueAsString(updateSubTopicRequest))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(subTopicService, times(1)).update(updateSubTopicRequest)
    }

    @Test
    fun `Should delete a sub topic successfully`() {
        val subTopic = getSubTopic(id = 1)
        val deleteSubTopicResponse = DeleteSubTopicResponse(subTopic, SUB_TOPIC_DELETED)

        `when`(subTopicService.delete(subTopic.id!!)).thenReturn(deleteSubTopicResponse)

        mockMvc.perform(
                delete("/sub-topic/delete/${subTopic.id!!}")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(deleteSubTopicResponse)))

        verify(subTopicService, times(1)).delete(subTopic.id!!)
    }

    @Test
    fun `Should fails while deleting a sub topic that doesn't exist`() {
        val subTopic = getSubTopic(id = 1)
        val errorMessage = SUB_TOPIC_NOT_FOUND + subTopic.id!!

        `when`(subTopicService.delete(subTopic.id!!)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
                delete("/sub-topic/delete/${subTopic.id!!}")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(subTopicService, times(1)).delete(subTopic.id!!)
    }
}
