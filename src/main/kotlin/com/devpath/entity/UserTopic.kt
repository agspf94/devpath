package com.devpath.entity

import javax.persistence.CascadeType.ALL
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class UserTopic(
    @Id
    @GeneratedValue
    var id: Int? = null,
    val topicId: Int,
    @OneToMany(cascade = [ALL])
    var userSubTopics: MutableSet<UserSubTopic>
)
