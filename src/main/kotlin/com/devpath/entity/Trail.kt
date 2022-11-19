package com.devpath.entity

import javax.persistence.CascadeType.ALL
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class Trail(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @Column(length = 2048)
    val name: String,
    val duration: Int,
    @Column(length = 2048)
    val description: String,
    @Column(length = 2048)
    val averageSalary: String,
    @OneToMany(cascade = [ALL])
    val jobs: MutableSet<Job>,
    @OneToMany(cascade = [ALL])
    val topics: MutableSet<Topic>
)
