package com.devpath.service

import com.devpath.constants.Constants.Companion.SUB_TOPIC_ALREADY_EXISTS
import com.devpath.constants.Constants.Companion.SUB_TOPIC_DELETED
import com.devpath.constants.Constants.Companion.SUB_TOPIC_LIST_IS_EMPTY
import com.devpath.constants.Constants.Companion.SUB_TOPIC_NOT_FOUND
import com.devpath.dto.subtopic.request.CreateSubTopicRequest
import com.devpath.dto.subtopic.request.UpdateSubTopicRequest
import com.devpath.dto.subtopic.response.DeleteSubTopicResponse
import com.devpath.entity.SubTopic
import com.devpath.exception.exceptions.EmptySubTopicListException
import com.devpath.exception.exceptions.SubTopicAlreadyExistsException
import com.devpath.repository.SubTopicRepository
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class SubTopicService(
    private val subTopicRepository: SubTopicRepository
) {
    fun create(createSubTopicRequest: CreateSubTopicRequest): SubTopic {
        subTopicRepository.findByName(createSubTopicRequest.name)
            .ifPresent { throw SubTopicAlreadyExistsException(SUB_TOPIC_ALREADY_EXISTS + createSubTopicRequest.name) }
        return subTopicRepository.saveAndFlush(createSubTopicRequest.toSubTopic())
    }

    fun read(id: Int): SubTopic {
        return subTopicRepository.findById(id)
            .map { it }
            .orElseThrow { NoSuchElementException(SUB_TOPIC_NOT_FOUND + id) }
    }

    fun readAll(): List<SubTopic> {
        return subTopicRepository.findAll()
            .stream()
            .collect(Collectors.toList())
            .ifEmpty { throw EmptySubTopicListException(SUB_TOPIC_LIST_IS_EMPTY) }
    }

    fun update(updateSubTopicRequest: UpdateSubTopicRequest): SubTopic {
        return subTopicRepository.findById(updateSubTopicRequest.id)
            .map {
                val updatedSubTopic = SubTopic(
                    id = it.id,
                    name = updateSubTopicRequest.name ?: it.name,
                    content = updateSubTopicRequest.content ?: it.content
                )
                subTopicRepository.saveAndFlush(updatedSubTopic)
                updatedSubTopic
            }
            .orElseThrow { NoSuchElementException(SUB_TOPIC_NOT_FOUND + updateSubTopicRequest.id) }
    }

    fun delete(id: Int): DeleteSubTopicResponse {
        return subTopicRepository.findById(id)
            .map {
                subTopicRepository.deleteById(it.id!!)
                DeleteSubTopicResponse(it, SUB_TOPIC_DELETED)
            }
            .orElseThrow { NoSuchElementException(SUB_TOPIC_NOT_FOUND + id) }
    }
}
