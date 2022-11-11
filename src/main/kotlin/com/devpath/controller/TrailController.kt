package com.devpath.controller

import com.devpath.dto.trail.request.UpdateTrailRequest
import com.devpath.dto.trail.response.DeleteTrailResponse
import com.devpath.entity.Trail
import com.devpath.service.TrailService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
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
    fun create(@RequestBody trail: Trail): Trail {
        return trailService.create(trail)
    }

    @GetMapping("/{id}")
    fun read(@PathVariable id: Int): Trail {
        return trailService.read(id)
    }

    @GetMapping("/all")
    fun readAll(): List<Trail> {
        return trailService.readAll()
    }

//    @PatchMapping("/update")
//    fun update(@RequestBody updateTrailRequest: UpdateTrailRequest): Trail {
//        return trailService.update(updateTrailRequest)
//    }

    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable id: Int): DeleteTrailResponse {
        return trailService.delete(id)
    }
}
