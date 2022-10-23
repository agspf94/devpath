package com.devpath.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "\"user\"")
data class User(
    @Id
    @GeneratedValue
    val id: Int? = null,
    val name: String,
    val email: String,
    val password: String,
    val isMentor: Boolean
)
