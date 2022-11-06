package com.devpath.service

import com.devpath.constants.Constants.Companion.SUB_TOPIC_NOT_FOUND
import com.devpath.entity.SubTopic
import com.devpath.repository.SubTopicRepository
import org.springframework.stereotype.Service

@Service
class SubTopicService(
    private val subTopicRepository: SubTopicRepository
) {
    fun read(id: Int): SubTopic {
        return subTopicRepository.findById(id)
            .map { it }
            .orElseThrow { NoSuchElementException(SUB_TOPIC_NOT_FOUND + id) }
    }
}
