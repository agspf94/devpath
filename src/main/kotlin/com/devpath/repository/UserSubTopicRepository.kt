package com.devpath.repository

import com.devpath.entity.UserSubTopic
import org.springframework.data.jpa.repository.JpaRepository

interface UserSubTopicRepository: JpaRepository<UserSubTopic, Int>
