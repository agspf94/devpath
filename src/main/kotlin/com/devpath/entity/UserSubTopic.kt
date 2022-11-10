package com.devpath.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class UserSubTopic(
    @Id
    @GeneratedValue
    var id: Int? = null,
    val subTopicId: Int,
    var active: Boolean
)
