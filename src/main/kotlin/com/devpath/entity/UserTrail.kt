package com.devpath.entity

import com.devpath.dto.trail.TrailDTO
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne

@Entity
data class UserTrail(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @OneToOne
    val trail: Trail,
    @OneToMany(cascade = [CascadeType.ALL])
    var userTopics: MutableSet<UserTopic>
) {
    fun toTrailDTO(): TrailDTO {
        return TrailDTO(
            id = trail.id!!,
            name = trail.name,
            duration = trail.duration,
            description = trail.description,
            averageSalary = trail.averageSalary,
            jobs = trail.jobs.map { it.toJobDTO() }.toMutableSet(),
            topics = userTopics.map { it.toTopicDTO(trail) }.toMutableSet()
        )
    }
}
