package com.devpath.repository

import com.devpath.entity.SubTopic
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface SubTopicRepository : JpaRepository<SubTopic, Int> {
    fun findByName(name: String): Optional<SubTopic>
}
