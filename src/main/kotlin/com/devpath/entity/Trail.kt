package com.devpath.entity

import org.hibernate.type.OrderedSetType
import javax.persistence.CascadeType.ALL
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class Trail(
    @Id
    @GeneratedValue
    var id: Int? = null,
    val name: String,
    val duration: Int,
    val description: String,
    val averageSalary: String,
    @OneToMany(cascade = [ALL])
    val jobs: MutableSet<Job>,
    @OneToMany(cascade = [ALL])
    val topics: MutableSet<Topic>
)
