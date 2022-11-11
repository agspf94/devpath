package com.devpath.entity

import javax.persistence.CascadeType.ALL
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class UserTrail(
    @Id
    @GeneratedValue(generator = "user_trail_generator")
    val id: Int? = null,
    val trailId: Int,
    @OneToMany(cascade = [ALL])
    var userTopics: MutableSet<UserTopic>
)
