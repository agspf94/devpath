package com.devpath.repository

import com.devpath.entity.Schedule
import org.springframework.data.jpa.repository.JpaRepository

interface ScheduleRepository: JpaRepository<Schedule, Int>
