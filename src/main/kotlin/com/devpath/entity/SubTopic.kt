package com.devpath.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class SubTopic(
    @Id
    @GeneratedValue
    val id: Int? = null,
    val name: String,
    val content: String
)
