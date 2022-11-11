package com.devpath.repository

import com.devpath.entity.Topic
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface TopicRepository : JpaRepository<Topic, Int> {
    fun findByName(name: String): Optional<Topic>
}
