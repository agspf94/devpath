package com.devpath.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Schedule(
    @Id
    @GeneratedValue
    val id: Int? = null,
    val weekDay: String,
    val period: String
)
