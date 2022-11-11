package com.devpath.entity

import javax.persistence.CascadeType.ALL
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OneToOne

@Entity
data class UserTrail(
    @Id
    @GeneratedValue(generator = "user_trail_generator")
    val id: Int? = null,
    @OneToOne
    val trail: Trail,
    @OneToMany(cascade = [ALL])
    var userTopics: MutableSet<UserTopic>
)
