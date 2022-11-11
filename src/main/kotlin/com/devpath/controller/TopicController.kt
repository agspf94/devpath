package com.devpath.controller

import com.devpath.dto.topic.request.CreateTopicRequest
import com.devpath.dto.topic.request.UpdateTopicRequest
import com.devpath.dto.topic.response.DeleteTopicResponse
import com.devpath.entity.Topic
import com.devpath.service.TopicService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/topic")
class TopicController(
    private val topicService: TopicService
){
    @PostMapping("/create")
    fun create(@RequestBody createTopicRequest: CreateTopicRequest): Topic {
        return topicService.create(createTopicRequest)
    }

    @GetMapping("/{id}")
    fun read(@PathVariable id: Int): Topic {
        return topicService.read(id)
    }

    @GetMapping("/all")
    fun readAll(): List<Topic> {
        return topicService.readAll()
    }

    @PatchMapping("/update")
    fun update(@RequestBody updateTopicRequest: UpdateTopicRequest): Topic {
        return topicService.update(updateTopicRequest)
    }

    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable id: Int): DeleteTopicResponse {
        return topicService.delete(id)
    }
}
