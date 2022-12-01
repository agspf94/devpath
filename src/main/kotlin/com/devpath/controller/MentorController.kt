package com.devpath.controller

import com.devpath.dto.mentor.request.UpdateMentorRequest
import com.devpath.dto.mentor.response.DeleteMentorResponse
import com.devpath.entity.Mentor
import com.devpath.entity.User
import com.devpath.service.MentorService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/mentor")
class MentorController(
    private val mentorService: MentorService
) {
    @PostMapping("/become-mentor/{userId}")
    fun becomeMentor(@PathVariable userId: Int): User {
        return mentorService.becomeMentor(userId)
    }

    @GetMapping("/{userId}")
    fun read(@PathVariable userId: Int): Mentor {
        return mentorService.read(userId)
    }

    @GetMapping("/all")
    fun readAll(): List<Mentor> {
        return mentorService.readAll()
    }

    @PatchMapping("/update")
    fun update(@RequestBody updateMentorRequest: UpdateMentorRequest): Mentor {
        return mentorService.update(updateMentorRequest)
    }

    @DeleteMapping("/delete/{userId}")
    fun delete(@PathVariable userId: Int): DeleteMentorResponse {
        return mentorService.delete(userId)
    }
}
