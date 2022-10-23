package com.devpath.controller

import com.devpath.dto.trail.response.DeleteTrailResponse
import com.devpath.entity.Trail
import com.devpath.service.TrailService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/trail")
class TrailController(
    private val trailService: TrailService
) {
    @PostMapping("/create")
    fun createTrail(@RequestBody trail: Trail): Trail {
        return trailService.createTrail(trail)
    }

    @GetMapping("/{id}")
    fun readTrail(@PathVariable id: Int): Trail {
        return trailService.readTrail(id)
    }

    @GetMapping("/all")
    fun readAllTrails(): List<Trail> {
        return trailService.readAllTrails()
    }

    @DeleteMapping("/delete/{id}")
    fun deleteTrail(@PathVariable id: Int): DeleteTrailResponse {
        return trailService.deleteTrail(id)
    }
}
