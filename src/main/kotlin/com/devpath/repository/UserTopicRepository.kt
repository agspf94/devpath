package com.devpath.repository

import com.devpath.entity.UserTopic
import org.springframework.data.jpa.repository.JpaRepository

interface UserTopicRepository: JpaRepository<UserTopic, Int>
