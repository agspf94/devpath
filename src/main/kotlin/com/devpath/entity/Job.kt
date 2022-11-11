package com.devpath.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Job(
    @Id
    @GeneratedValue(generator = "job_generator")
    val id: Int? = null,
    val title: String,
    val location: String,
    val period: String,
    val role: String,
    val link: String
)
