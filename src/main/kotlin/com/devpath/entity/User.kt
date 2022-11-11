package com.devpath.entity

import javax.persistence.CascadeType.ALL
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "\"user\"")
data class User(
    @Id
    @GeneratedValue(generator = "user_generator")
    val id: Int? = null,
    val name: String,
    val email: String,
    val password: String,
    var isMentor: Boolean,
    @OneToMany(cascade = [ALL])
    var userTrails: MutableSet<UserTrail>
)
