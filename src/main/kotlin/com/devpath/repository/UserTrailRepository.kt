package com.devpath.repository

import com.devpath.entity.UserTrail
import org.springframework.data.jpa.repository.JpaRepository

interface UserTrailRepository: JpaRepository<UserTrail, Int>
