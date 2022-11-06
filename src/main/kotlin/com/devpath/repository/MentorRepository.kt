package com.devpath.repository

import com.devpath.entity.Mentor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MentorRepository : JpaRepository<Mentor, Int>
