package com.devpath.entity

import com.devpath.dto.user.UserDTO
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "\"user\"")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @Column(length = 2048)
    val name: String,
    @Column(length = 2048)
    val email: String,
    @Column(length = 2048)
    val password: String,
    var mentorStatus: String,
    @OneToMany(cascade = [CascadeType.ALL])
    var userTrails: MutableSet<UserTrail> = mutableSetOf(),
    @OneToMany(cascade = [CascadeType.ALL])
    var schedules: MutableSet<Schedule> = mutableSetOf()
) {
    fun toUserDTO(): UserDTO {
        return UserDTO(
            id = id!!,
            name = name,
            email = email,
            password = password,
            mentorStatus = mentorStatus,
            trails = userTrails.map { it.toTrailDTO() }.toMutableSet(),
            schedules = schedules
        )
    }
}
