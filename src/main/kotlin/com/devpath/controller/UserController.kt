package com.devpath.controller

import com.devpath.dto.user.request.CreateUserRequest
import com.devpath.dto.user.request.UpdateTrailStatusRequest
import com.devpath.dto.user.request.UpdateUserRequest
import com.devpath.dto.user.response.DeleteUserResponse
import com.devpath.entity.User
import com.devpath.service.UserService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/create")
    fun create(@RequestBody createUserRequest: CreateUserRequest): User {
        return userService.create(createUserRequest)
    }

    @GetMapping("/{email}")
    fun read(@PathVariable email: String): User {
        return userService.read(email)
    }

    @PatchMapping("/update")
    fun update(@RequestBody updateUserRequest: UpdateUserRequest): User {
        return userService.update(updateUserRequest)
    }

    @DeleteMapping("/delete/{email}")
    fun delete(@PathVariable email: String): DeleteUserResponse {
        return userService.delete(email)
    }

    @PostMapping("/{userEmail}/add-trail/{trailId}")
    fun addTrail(
        @PathVariable userEmail: String,
        @PathVariable trailId: Int
    ): User {
        return userService.addTrail(userEmail, trailId)
    }

    @PatchMapping("/update-trail")
    fun updateTrailStatus(@RequestBody updateTrailStatusRequest: UpdateTrailStatusRequest): User {
        return userService.updateTrailStatus(updateTrailStatusRequest)
    }
}
