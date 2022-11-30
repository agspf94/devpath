package com.devpath.repository

import com.devpath.entity.Job
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface JobRepository : JpaRepository<Job, Int> {
    fun findByTitle(title: String): Optional<Job>
}
