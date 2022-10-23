package com.devpath.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Trail(
    @Id
    @GeneratedValue
    var id: Int? = null,
    var name: String
)
