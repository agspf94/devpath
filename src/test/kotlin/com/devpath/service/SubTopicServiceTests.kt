package com.devpath.service

import com.devpath.constants.Constants.Companion
import com.devpath.constants.Constants.Companion.SUB_TOPIC_ALREADY_EXISTS
import com.devpath.constants.Constants.Companion.SUB_TOPIC_LIST_IS_EMPTY
import com.devpath.constants.Constants.Companion.SUB_TOPIC_NOT_FOUND
import com.devpath.entity.SubTopic
import com.devpath.exception.exceptions.EmptySubTopicListException
import com.devpath.exception.exceptions.SubTopicAlreadyExistsException
import com.devpath.mock.SubTopicMockProvider.Companion.getCreateSubTopicRequest
import com.devpath.mock.SubTopicMockProvider.Companion.getSubTopic
import com.devpath.mock.SubTopicMockProvider.Companion.getUpdateSubTopicRequest
import com.devpath.repository.SubTopicRepository
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
class SubTopicServiceTests {
    @MockBean
    private lateinit var subTopicRepository: SubTopicRepository

    @Autowired
    private lateinit var subTopicService: SubTopicService

    @Test
    fun `Sub topic service should not be null`() {
        assertNotNull(subTopicService)
    }

    @Test
    fun `Create sub topic should return the new sub topic`() {
        val createSubTopicRequest = getCreateSubTopicRequest()
        val subTopic = getSubTopic(id = 1)

        `when`(subTopicRepository.findByName(createSubTopicRequest.name)).thenReturn(Optional.empty())
        `when`(subTopicRepository.saveAndFlush(any())).thenReturn(subTopic)

        val savedSubTopic = subTopicService.create(createSubTopicRequest)

        assertAttributes(subTopic, savedSubTopic)

        verify(subTopicRepository, times(1)).findByName(createSubTopicRequest.name)
        verify(subTopicRepository, times(1)).saveAndFlush(any())
    }

    @Test
    fun `Create sub topic should throw an exception when the sub topic already exists`() {
        val createSubTopicRequest = getCreateSubTopicRequest()
        val subTopic = getSubTopic(id = 1)

        `when`(subTopicRepository.findByName(createSubTopicRequest.name)).thenReturn(Optional.of(subTopic))

        val exception = assertThrows<SubTopicAlreadyExistsException> { subTopicService.create(createSubTopicRequest) }
        assertEquals(SUB_TOPIC_ALREADY_EXISTS + createSubTopicRequest.name, exception.message)

        verify(subTopicRepository, times(1)).findByName(createSubTopicRequest.name)
        verify(subTopicRepository, times(0)).saveAndFlush(any())
    }

    @Test
    fun `Read should return the desired sub topic`() {
        val subTopic = getSubTopic(id = 1)

        `when`(subTopicRepository.findById(subTopic.id!!)).thenReturn(Optional.of(subTopic))

        val savedSubTopic = subTopicService.read(subTopic.id!!)

        assertAttributes(subTopic, savedSubTopic)

        verify(subTopicRepository, times(1)).findById(subTopic.id!!)
    }

    @Test
    fun `Read should throw an exception when the sub topic doesn't exist`() {
        val subTopic = getSubTopic(id = 1)

        `when`(subTopicRepository.findById(subTopic.id!!)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> { subTopicService.read(subTopic.id!!) }
        assertEquals(Companion.SUB_TOPIC_NOT_FOUND + subTopic.id, exception.message)

        verify(subTopicRepository, times(1)).findById(subTopic.id!!)
    }

    @Test
    fun `Read all should return all sub topics list`() {
        val subTopic1 = getSubTopic(id = 1)
        val subTopic2 = getSubTopic(id = 2)

        `when`(subTopicRepository.findAll()).thenReturn(listOf(subTopic1, subTopic2))

        val subTopicsList = subTopicService.readAll()

        assertThat(subTopicsList)
            .hasSize(2)
            .contains(subTopic1, subTopic2)

        verify(subTopicRepository, times(1)).findAll()
    }

    @Test
    fun `Read all should throw an exception when all sub topics list is empty`() {
        `when`(subTopicRepository.findAll()).thenReturn(listOf())

        val exception = assertThrows<EmptySubTopicListException> { subTopicService.readAll() }
        assertEquals(SUB_TOPIC_LIST_IS_EMPTY, exception.message)

        verify(subTopicRepository, times(1)).findAll()
    }

    @Test
    fun `Update should update the sub topic and return it updated`() {
        val updateSubTopicRequest = getUpdateSubTopicRequest(id = 1)
        val subTopic = getSubTopic(id = 1)

        `when`(subTopicRepository.findById(updateSubTopicRequest.id)).thenReturn(Optional.of(subTopic))
        `when`(subTopicRepository.saveAndFlush(any())).thenReturn(subTopic)

        val updatedSubTopic = subTopicService.update(updateSubTopicRequest)

        assertEquals(updateSubTopicRequest.name, updatedSubTopic.name)
        assertEquals(updateSubTopicRequest.content, updatedSubTopic.content)

        verify(subTopicRepository, times(1)).findById(updateSubTopicRequest.id)
        verify(subTopicRepository, times(1)).saveAndFlush(any())
    }

    @Test
    fun `Update should throw an exception when the sub topic doesn't exist`() {
        val updateSubTopicRequest = getUpdateSubTopicRequest(id = 1)

        `when`(subTopicRepository.findById(updateSubTopicRequest.id)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> { subTopicService.update(updateSubTopicRequest) }
        assertEquals(SUB_TOPIC_NOT_FOUND + updateSubTopicRequest.id, exception.message)

        verify(subTopicRepository, times(1)).findById(updateSubTopicRequest.id)
        verify(subTopicRepository, times(0)).saveAndFlush(any())
    }

    @Test
    fun `Delete should delete the desired sub topic`() {
        val subTopic = getSubTopic(id = 1)

        `when`(subTopicRepository.findById(subTopic.id!!)).thenReturn(Optional.of(subTopic))
        doNothing().`when`(subTopicRepository).deleteById(subTopic.id!!)

        val deleteSubTopicResponse = subTopicService.delete(subTopic.id!!)

        assertEquals(subTopic, deleteSubTopicResponse.subTopic)
        assertEquals(Companion.SUB_TOPIC_DELETED, deleteSubTopicResponse.message)

        verify(subTopicRepository, times(1)).findById(subTopic.id!!)
        verify(subTopicRepository, times(1)).deleteById(subTopic.id!!)
    }

    @Test
    fun `Delete should throw an exception when the sub topic doesn't exist`() {
        val subTopic = getSubTopic(id = 1)

        `when`(subTopicRepository.findById(subTopic.id!!)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> { subTopicService.delete(subTopic.id!!) }
        assertEquals(SUB_TOPIC_NOT_FOUND + subTopic.id!!, exception.message)

        verify(subTopicRepository, times(1)).findById(subTopic.id!!)
        verify(subTopicRepository, times(0)).deleteById(any())
    }

    private fun assertAttributes(expectedSubTopic: SubTopic, savedSubTopic: SubTopic) {
        assertNotNull(expectedSubTopic.id)
        assertEquals(expectedSubTopic.name, savedSubTopic.name)
        assertEquals(expectedSubTopic.content, savedSubTopic.content)
    }
}
