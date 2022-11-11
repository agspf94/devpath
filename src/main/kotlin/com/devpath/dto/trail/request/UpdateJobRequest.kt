package com.devpath.dto.trail.request

data class UpdateJobRequest(
    val id: Int,
    var title: String? = null,
    var location: String? = null,
    var period: String? = null,
    var role: String? = null,
    var link: String? = null
)
