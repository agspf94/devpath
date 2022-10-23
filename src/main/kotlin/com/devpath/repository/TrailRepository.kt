package com.devpath.repository

import com.devpath.entity.Trail
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface TrailRepository : JpaRepository<Trail, Int> {
    fun findByName(name: String): Optional<Trail>
}
