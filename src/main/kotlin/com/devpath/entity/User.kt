package com.devpath.entity

import com.devpath.dto.user.UserDTO
import javax.persistence.CascadeType.ALL
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "\"user\"")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @Column(length = 2048)
    val name: String,
    @Column(length = 2048)
    val email: String,
    @Column(length = 2048)
    val password: String,
    var isMentor: Boolean,
    @OneToMany(cascade = [ALL])
    var userTrails: MutableSet<UserTrail>
) {
    fun toUserDTO(): UserDTO {
        return UserDTO(
            id = id!!,
            name = name,
            email = email,
            password = password,
            isMentor = isMentor,
            trails = userTrails.map { it.toTrailDTO() }.toMutableSet()
        )
    }
}
