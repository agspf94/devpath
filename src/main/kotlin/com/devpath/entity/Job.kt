package com.devpath.entity

import com.devpath.dto.job.JobDTO
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Job(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @Column(length = 2048)
    val title: String,
    @Column(length = 2048)
    val location: String,
    @Column(length = 2048)
    val period: String,
    @Column(length = 2048)
    val role: String,
    @Column(length = 2048)
    val link: String
) {
    fun toJobDTO(): JobDTO {
        return JobDTO(
            id = id!!,
            title = title,
            location = location,
            period = period,
            role = role,
            link = link
        )
    }
}
