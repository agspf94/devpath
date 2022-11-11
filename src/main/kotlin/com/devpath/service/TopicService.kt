package com.devpath.service

import com.devpath.constants.Constants.Companion.TOPIC_ALREADY_EXISTS
import com.devpath.constants.Constants.Companion.TOPIC_DELETED
import com.devpath.constants.Constants.Companion.TOPIC_LIST_IS_EMPTY
import com.devpath.constants.Constants.Companion.TOPIC_NOT_FOUND_ID
import com.devpath.constants.Constants.Companion.TOPIC_NOT_FOUND_NAME
import com.devpath.dto.topic.request.CreateTopicRequest
import com.devpath.dto.topic.request.UpdateTopicRequest
import com.devpath.dto.topic.response.DeleteTopicResponse
import com.devpath.entity.SubTopic
import com.devpath.entity.Topic
import com.devpath.exception.exceptions.EmptyTopicListException
import com.devpath.exception.exceptions.TopicAlreadyExistsException
import com.devpath.repository.TopicRepository
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class TopicService(
    private val topicRepository: TopicRepository,
    private val subTopicService: SubTopicService
) {
    fun create(createTopicRequest: CreateTopicRequest): Topic {
        topicRepository.findByName(createTopicRequest.name)
            .ifPresent { throw TopicAlreadyExistsException(TOPIC_ALREADY_EXISTS + createTopicRequest.name) }
        return topicRepository.saveAndFlush(createTopicRequest.toTopic())
    }

    fun read(id: Int): Topic {
        return topicRepository.findById(id)
            .map { it }
            .orElseThrow { NoSuchElementException(TOPIC_NOT_FOUND_ID + id) }
    }

    fun readAll(): List<Topic> {
        return topicRepository.findAll()
            .stream()
            .collect(Collectors.toList())
            .ifEmpty { throw EmptyTopicListException(TOPIC_LIST_IS_EMPTY) }
    }

    fun update(updateTopicRequest: UpdateTopicRequest): Topic {
        return topicRepository.findById(updateTopicRequest.id)
            .map {
                topicRepository.saveAndFlush(
                    Topic(
                        id = it.id,
                        name = updateTopicRequest.name ?: it.name,
                        subTopics = updateSubTopics(it, updateTopicRequest)
                    )
                )
            }
            .orElseThrow { NoSuchElementException(TOPIC_NOT_FOUND_NAME + updateTopicRequest.name) }
    }

    fun delete(id: Int): DeleteTopicResponse {
        return topicRepository.findById(id)
            .map {
                topicRepository.deleteById(it.id!!)
                DeleteTopicResponse(it, TOPIC_DELETED)
            }
            .orElseThrow { NoSuchElementException(TOPIC_NOT_FOUND_ID + id) }
    }

    private fun updateSubTopics(topic: Topic, updateTopicRequest: UpdateTopicRequest): MutableSet<SubTopic> {
        return if (updateTopicRequest.subTopicsIds == null)
            topic.subTopics
        else {
            val subTopics = mutableSetOf<SubTopic>()
            updateTopicRequest.subTopicsIds?.map {
                val subTopic = subTopicService.read(it)
                subTopics.add(subTopic)
            }
            subTopics
        }
    }
}
