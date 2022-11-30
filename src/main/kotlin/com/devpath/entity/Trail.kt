package com.devpath.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

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
    @OneToMany(cascade = [CascadeType.ALL])
    val jobs: MutableSet<Job>,
    @OneToMany(cascade = [CascadeType.ALL])
    val topics: MutableSet<Topic>
)
