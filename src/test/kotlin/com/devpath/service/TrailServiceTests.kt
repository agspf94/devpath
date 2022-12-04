package com.devpath.service

import com.devpath.constants.Constants.Companion.EMPTY_WORDS_LIST
import com.devpath.constants.Constants.Companion.NO_TRAILS_WERE_FOUND
import com.devpath.constants.Constants.Companion.TRAIL_ALREADY_EXISTS
import com.devpath.constants.Constants.Companion.TRAIL_DELETED
import com.devpath.constants.Constants.Companion.TRAIL_LIST_IS_EMPTY
import com.devpath.constants.Constants.Companion.TRAIL_NOT_FOUND
import com.devpath.entity.Trail
import com.devpath.exception.exceptions.trail.EmptyTrailListException
import com.devpath.exception.exceptions.trail.EmptyWordsListException
import com.devpath.exception.exceptions.trail.NoTrailsWereFoundException
import com.devpath.exception.exceptions.trail.TrailAlreadyExistsException
import com.devpath.mock.TrailMockProvider.Companion.getCreateTrailRequest
import com.devpath.mock.TrailMockProvider.Companion.getTrail
import com.devpath.mock.TrailMockProvider.Companion.getUpdateTrailRequest
import com.devpath.repository.TrailRepository
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
class TrailServiceTests {
    @MockBean
    private lateinit var trailRepository: TrailRepository

    @Autowired
    private lateinit var trailService: TrailService

    @Test
    fun `Trail service should not be null`() {
        assertNotNull(trailService)
    }

    @Test
    fun `Create trail should return the new trail`() {
        val createTrailRequest = getCreateTrailRequest()
        val trail = getTrail(id = 1)

        `when`(trailRepository.findByName(createTrailRequest.name)).thenReturn(Optional.empty())
        `when`(trailRepository.saveAndFlush(any())).thenReturn(trail)

        val savedTrail = trailService.create(createTrailRequest)

        assertAttributes(trail, savedTrail)

        verify(trailRepository, times(1)).findByName(createTrailRequest.name)
        verify(trailRepository, times(1)).saveAndFlush(any())
    }

    @Test
    fun `Create trail should throw an exception when the trail already exists`() {
        val createTrailRequest = getCreateTrailRequest()
        val trail = getTrail(id = 1)

        `when`(trailRepository.findByName(createTrailRequest.name)).thenReturn(Optional.of(trail))

        val exception = assertThrows<TrailAlreadyExistsException> { trailService.create(createTrailRequest) }
        assertEquals(TRAIL_ALREADY_EXISTS + createTrailRequest.name, exception.message)

        verify(trailRepository, times(1)).findByName(createTrailRequest.name)
        verify(trailRepository, times(0)).saveAndFlush(any())
    }

    @Test
    fun `Read should return the desired trail`() {
        val trail = getTrail(id = 1)

        `when`(trailRepository.findById(trail.id!!)).thenReturn(Optional.of(trail))

        val savedTrail = trailService.read(trail.id!!)

        assertAttributes(trail, savedTrail)

        verify(trailRepository, times(1)).findById(trail.id!!)
    }

    @Test
    fun `Read should throw an exception when the trail doesn't exist`() {
        val trail = getTrail(id = 1)

        `when`(trailRepository.findById(trail.id!!)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> { trailService.read(trail.id!!) }
        assertEquals(TRAIL_NOT_FOUND + trail.id, exception.message)

        verify(trailRepository, times(1)).findById(trail.id!!)
    }

    @Test
    fun `Read all should return all trails list`() {
        val trail1 = getTrail(id = 1)
        val trail2 = getTrail(id = 2)

        `when`(trailRepository.findAll()).thenReturn(listOf(trail1, trail2))

        val trailsList = trailService.readAll()

        assertThat(trailsList)
            .hasSize(2)
            .contains(trail1, trail2)

        verify(trailRepository, times(1)).findAll()
    }

    @Test
    fun `Read all should throw an exception when all trails list is empty`() {
        `when`(trailRepository.findAll()).thenReturn(listOf())

        val exception = assertThrows<EmptyTrailListException> { trailService.readAll() }
        assertEquals(TRAIL_LIST_IS_EMPTY, exception.message)

        verify(trailRepository, times(1)).findAll()
    }

    @Test
    fun `Update should update the trail and return it updated`() {
        val updateTrailRequest = getUpdateTrailRequest(id = 1)
        val trail = getTrail(id = 1)

        `when`(trailRepository.findById(updateTrailRequest.id)).thenReturn(Optional.of(trail))
        `when`(trailRepository.saveAndFlush(any())).thenReturn(trail)

        val updatedTrail = trailService.update(updateTrailRequest)

        assertEquals(updateTrailRequest.name, updatedTrail.name)
        assertEquals(updateTrailRequest.duration, updatedTrail.duration)
        assertEquals(updateTrailRequest.description, updatedTrail.description)
        assertEquals(updateTrailRequest.averageSalary, updatedTrail.averageSalary)

        verify(trailRepository, times(1)).findById(updateTrailRequest.id)
        verify(trailRepository, times(1)).saveAndFlush(any())
    }

    @Test
    fun `Update should throw an exception when the trail doesn't exist`() {
        val updateTrailRequest = getUpdateTrailRequest(id = 1)

        `when`(trailRepository.findById(updateTrailRequest.id)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> { trailService.update(updateTrailRequest) }
        assertEquals(TRAIL_NOT_FOUND + updateTrailRequest.id, exception.message)

        verify(trailRepository, times(1)).findById(updateTrailRequest.id)
        verify(trailRepository, times(0)).saveAndFlush(any())
    }

    @Test
    fun `Delete should delete the desired trail`() {
        val trail = getTrail(id = 1)

        `when`(trailRepository.findById(trail.id!!)).thenReturn(Optional.of(trail))
        doNothing().`when`(trailRepository).deleteById(trail.id!!)

        val deleteTrailResponse = trailService.delete(trail.id!!)

        assertEquals(trail, deleteTrailResponse.trail)
        assertEquals(TRAIL_DELETED, deleteTrailResponse.message)

        verify(trailRepository, times(1)).findById(trail.id!!)
        verify(trailRepository, times(1)).deleteById(trail.id!!)
    }

    @Test
    fun `Delete should throw an exception when the trail doesn't exist`() {
        val trail = getTrail(id = 1)

        `when`(trailRepository.findById(trail.id!!)).thenReturn(Optional.empty())

        val exception = assertThrows<NoSuchElementException> { trailService.delete(trail.id!!) }
        assertEquals(TRAIL_NOT_FOUND + trail.id!!, exception.message)

        verify(trailRepository, times(1)).findById(trail.id!!)
        verify(trailRepository, times(0)).deleteById(any())
    }

    @Test
    fun `Search should return the trails list matching the words list with 2 trails`() {
        val trail1 = getTrail(id = 1, name = "name1")
        val trail2 = getTrail(id = 2, name = "name2")
        val trailsList = mutableListOf(trail1, trail2)
        val words = "name1 name2"

        `when`(trailRepository.findAll()).thenReturn(trailsList)

        val trailsListResponse = trailService.search(words)

        assertThat(trailsListResponse.toList())
            .hasSize(2)
            .contains(trail1, trail2)

        verify(trailRepository, times(1)).findAll()
    }

    @Test
    fun `Search should return the trails list matching the words list with only one trail`() {
        val trail1 = getTrail(id = 1, name = "name1")
        val trail2 = getTrail(id = 2, name = "name2")
        val trailsList = mutableListOf(trail1, trail2)
        val words = "name1"

        `when`(trailRepository.findAll()).thenReturn(trailsList)

        val trailsListResponse = trailService.search(words)

        assertThat(trailsListResponse.toList())
            .hasSize(1)
            .contains(trail1)

        verify(trailRepository, times(1)).findAll()
    }

    @Test
    fun `Search should throw an exception when the words list is empty`() {
        val words = " "

        val exception = assertThrows<EmptyWordsListException> { trailService.search(words) }
        assertEquals(EMPTY_WORDS_LIST, exception.message)

        verify(trailRepository, times(0)).findAll()
    }

    @Test
    fun `Search should throw an exception when the trails list is empty`() {
        val trailsList = mutableListOf<Trail>()
        val words = "name1 name2"

        `when`(trailRepository.findAll()).thenReturn(trailsList)

        val exception = assertThrows<EmptyTrailListException> { trailService.search(words) }
        assertEquals(TRAIL_LIST_IS_EMPTY, exception.message)

        verify(trailRepository, times(1)).findAll()
    }

    @Test
    fun `Search should throw an exception when no trails were found with the given words list`() {
        val trail1 = getTrail(id = 1, name = "name1")
        val trail2 = getTrail(id = 2, name = "name2")
        val trailsList = mutableListOf(trail1, trail2)
        val words = "java"

        `when`(trailRepository.findAll()).thenReturn(trailsList)

        val exception = assertThrows<NoTrailsWereFoundException> { trailService.search(words) }
        assertEquals(NO_TRAILS_WERE_FOUND, exception.message)

        verify(trailRepository, times(1)).findAll()
    }

    private fun assertAttributes(expectedTrail: Trail, savedTrail: Trail) {
        assertNotNull(expectedTrail.id)
        assertEquals(expectedTrail.name, savedTrail.name)
        assertEquals(expectedTrail.duration, savedTrail.duration)
        assertEquals(expectedTrail.description, savedTrail.description)
        assertEquals(expectedTrail.averageSalary, savedTrail.averageSalary)
        assertEquals(expectedTrail.jobs, savedTrail.jobs)
        assertEquals(expectedTrail.topics, savedTrail.topics)
    }
}
