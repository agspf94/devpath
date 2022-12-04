package com.devpath.controller

import com.devpath.constants.Constants
import com.devpath.constants.Constants.Companion.NO_TRAILS_WERE_FOUND
import com.devpath.constants.Constants.Companion.TRAIL_ALREADY_EXISTS
import com.devpath.constants.Constants.Companion.TRAIL_DELETED
import com.devpath.constants.Constants.Companion.TRAIL_NOT_FOUND
import com.devpath.dto.trail.response.DeleteTrailResponse
import com.devpath.exception.ErrorMessage
import com.devpath.exception.exceptions.trail.EmptyTrailListException
import com.devpath.exception.exceptions.trail.NoTrailsWereFoundException
import com.devpath.exception.exceptions.trail.TrailAlreadyExistsException
import com.devpath.mock.TrailMockProvider.Companion.getCreateTrailRequest
import com.devpath.mock.TrailMockProvider.Companion.getTrail
import com.devpath.mock.TrailMockProvider.Companion.getUpdateTrailRequest
import com.devpath.service.TrailService
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(TrailController::class)
class TrailControllerTest {
    @MockBean
    private lateinit var trailService: TrailService

    @Autowired
    private lateinit var trailController: TrailController

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `Trail controller should not be null`() {
        assertNotNull(trailController)
    }

    @Test
    fun `Should create a trail successfully`() {
        val createTrailRequest = getCreateTrailRequest()
        val trail = getTrail(id = 1)

        `when`(trailService.create(createTrailRequest)).thenReturn(trail)

        mockMvc.perform(
            post("/trail/create")
                .accept(APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(createTrailRequest))
                .contentType(APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(trail)))

        verify(trailService, times(1)).create(createTrailRequest)
    }

    @Test
    fun `Should fail while creating a trail with a name that already exists`() {
        val createTrailRequest = getCreateTrailRequest()
        val errorMessage = TRAIL_ALREADY_EXISTS + createTrailRequest.name

        `when`(trailService.create(createTrailRequest)).thenAnswer { throw TrailAlreadyExistsException(errorMessage) }

        mockMvc.perform(
            post("/trail/create")
                .accept(APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(createTrailRequest))
                .contentType(APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(trailService, times(1)).create(createTrailRequest)
    }

    @Test
    fun `Should read a trail successfully`() {
        val trail = getTrail(id = 1)

        `when`(trailService.read(trail.id!!)).thenReturn(trail)

        mockMvc.perform(
            get("/trail/${trail.id!!}")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(trail)))

        verify(trailService, times(1)).read(trail.id!!)
    }

    @Test
    fun `Should fail while reading a trail that doesn't exist`() {
        val trail = getTrail(id = 1)
        val errorMessage = TRAIL_NOT_FOUND + trail.id

        `when`(trailService.read(trail.id!!)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
            get("/trail/${trail.id!!}")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(trailService, times(1)).read(trail.id!!)
    }

    @Test
    fun `Should read all trails list successfully`() {
        val trailsList = listOf(getTrail(id = 1), getTrail(id = 2))

        `when`(trailService.readAll()).thenReturn(trailsList)

        mockMvc.perform(
            get("/trail/all")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(trailsList)))

        verify(trailService, times(1)).readAll()
    }

    @Test
    fun `Should fail while reading all trails list because there aren't any`() {
        val errorMessage = Constants.TRAIL_LIST_IS_EMPTY

        `when`(trailService.readAll()).thenAnswer { throw EmptyTrailListException(errorMessage) }

        mockMvc.perform(
            get("/trail/all")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(trailService, times(1)).readAll()
    }

    @Test
    fun `Should update a trail successfully`() {
        val updateTrailRequest = getUpdateTrailRequest(id = 1)
        val trail = getTrail(id = 1)

        `when`(trailService.update(updateTrailRequest)).thenReturn(trail)

        mockMvc.perform(
            patch("/trail/update")
                .accept(APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(updateTrailRequest))
                .contentType(APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(trail)))

        verify(trailService, times(1)).update(updateTrailRequest)
    }

    @Test
    fun `Should fail while updating a trail that doesn't exist`() {
        val updateTrailRequest = getUpdateTrailRequest(id = 1)
        val errorMessage = TRAIL_NOT_FOUND + updateTrailRequest.name

        `when`(trailService.update(updateTrailRequest)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
            patch("/trail/update")
                .accept(APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(updateTrailRequest))
                .contentType(APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(trailService, times(1)).update(updateTrailRequest)
    }

    @Test
    fun `Should delete a trail successfully`() {
        val trail = getTrail(id = 1)
        val deleteTrailResponse = DeleteTrailResponse(trail, TRAIL_DELETED)

        `when`(trailService.delete(trail.id!!)).thenReturn(deleteTrailResponse)

        mockMvc.perform(
            delete("/trail/delete/${trail.id!!}")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(deleteTrailResponse)))

        verify(trailService, times(1)).delete(trail.id!!)
    }

    @Test
    fun `Should fail while deleting a trail that doesn't exist`() {
        val trail = getTrail(id = 1)
        val errorMessage = TRAIL_NOT_FOUND + trail.id!!

        `when`(trailService.delete(trail.id!!)).thenAnswer { throw NoSuchElementException(errorMessage) }

        mockMvc.perform(
            delete("/trail/delete/${trail.id!!}")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(trailService, times(1)).delete(trail.id!!)
    }

    @Test
    fun `Search should return all trails matching the words list`() {
        val trail1 = getTrail(id = 1, name = "name1")
        val trail2 = getTrail(id = 2, name = "name2")
        val trailsList = mutableSetOf(trail1, trail2)
        val words = "name1 name2"

        `when`(trailService.search(words)).thenReturn(trailsList)

        mockMvc.perform(
            get("/trail/search/$words")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(trailsList)))

        verify(trailService, times(1)).search(words)
    }

//    @Test
//    fun `Search should throw an exception when the words list is empty`() {
//        val words = " "
//        val errorMessage = EMPTY_WORDS_LIST
//
//        `when`(trailService.search(words)).thenAnswer { throw EmptyWordsListException(errorMessage) }
//
//        mockMvc.perform(
//            get("/trail/search/$words")
//                .accept(APPLICATION_JSON)
//                .contentType(APPLICATION_JSON)
//        )
//            .andDo(print())
//            .andExpect(status().is4xxClientError)
//            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))
//
//        verify(trailService, times(1)).search(words)
//    }

    @Test
    fun `Search should throw an exception when the trails list is empty`() {
        val words = "java"
        val errorMessage = NO_TRAILS_WERE_FOUND

        `when`(trailService.search(words)).thenAnswer { throw NoTrailsWereFoundException(errorMessage) }

        mockMvc.perform(
            get("/trail/search/$words")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(ErrorMessage(errorMessage))))

        verify(trailService, times(1)).search(words)
    }
}
