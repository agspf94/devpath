package com.devpath.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.OrderBy

@Entity
data class Mentor(
    @Id
    val id: Int? = null,
    @OneToOne
    val user: User,
    @Column(length = 2048)
    val role: String,
    val yearsOfExperience: Int,
    val hourCost: Int,
    @OneToMany
    val payments: MutableSet<Payment>,
    @OneToMany
    @OrderBy("date ASC")
    val schedules: MutableSet<Schedule>
)
