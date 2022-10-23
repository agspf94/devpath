package com.devpath.entity

import javax.persistence.CascadeType.ALL
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class Topic(
    @Id
    @GeneratedValue
    var id: Int? = null,
    val name: String,
    @OneToMany(cascade = [ALL])
    val subTopics: Set<SubTopic>,
    val active: Boolean
)
