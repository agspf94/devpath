package com.devpath.controller

import com.devpath.dto.subtopic.request.CreateSubTopicRequest
import com.devpath.dto.subtopic.request.UpdateSubTopicRequest
import com.devpath.dto.subtopic.response.DeleteSubTopicResponse
import com.devpath.entity.SubTopic
import com.devpath.service.SubTopicService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/subtopic")
class SubTopicController(
    private val subTopicService: SubTopicService
){
    @PostMapping("/create")
    fun create(@RequestBody createSubTopicRequest: CreateSubTopicRequest): SubTopic {
        return subTopicService.create(createSubTopicRequest)
    }

    @GetMapping("/{id}")
    fun read(@PathVariable id: Int): SubTopic {
        return subTopicService.read(id)
    }

    @GetMapping("/all")
    fun readAll(): List<SubTopic> {
        return subTopicService.readAll()
    }

    @PatchMapping("/update")
    fun update(@RequestBody updateSubTopicRequest: UpdateSubTopicRequest): SubTopic {
        return subTopicService.update(updateSubTopicRequest)
    }

    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable id: Int): DeleteSubTopicResponse {
        return subTopicService.delete(id)
    }
}
