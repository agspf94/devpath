package com.devpath.service

import com.devpath.constants.Constants.Companion.TOPIC_NOT_FOUND
import com.devpath.entity.Topic
import com.devpath.repository.TopicRepository
import org.springframework.stereotype.Service

@Service
class TopicService(
    private val topicRepository: TopicRepository
) {
    fun read(id: Int): Topic {
        return topicRepository.findById(id)
            .map { it }
            .orElseThrow { NoSuchElementException(TOPIC_NOT_FOUND + id) }
    }
}
