package com.devpath.entity

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class Payment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @ManyToOne
    val mentor: Mentor,
    @ManyToOne
    val user: User,
    val value: Int,
    val dateTime: LocalDateTime? = null
)
