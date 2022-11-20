package com.devpath.repository

import com.devpath.entity.Mentor
import com.devpath.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface MentorRepository : JpaRepository<Mentor, Int> {
    fun findByUser(user: User): Optional<Mentor>
}
