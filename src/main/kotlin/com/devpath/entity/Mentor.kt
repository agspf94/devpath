package com.devpath.entity

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OneToOne

@Entity
data class Mentor(
    @Id
    val id: Int? = null,
    @OneToOne
    val user: User,
    val role: String,
    val yearsOfExperience: Int,
    val hourCost: Int,
    @OneToMany
    val payments: MutableSet<Payment>,
    @OneToMany
    val schedules: MutableSet<Schedule>
)
