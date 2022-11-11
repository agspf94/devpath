package com.devpath.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class UserSubTopic(
    @Id
    @GeneratedValue(generator = "user_sub_topic_generator")
    val id: Int? = null,
    val subTopicId: Int,
    var active: Boolean
)
