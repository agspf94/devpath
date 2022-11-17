package com.devpath.entity

import com.devpath.dto.trail.TrailDTO
import javax.persistence.CascadeType.ALL
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OneToOne

@Entity
data class UserTrail(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @OneToOne
    val trail: Trail,
    @OneToMany(cascade = [ALL])
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
