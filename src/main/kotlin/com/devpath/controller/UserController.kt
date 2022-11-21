package com.devpath.controller

import com.devpath.dto.user.UserDTO
import com.devpath.dto.user.request.CreateUserRequest
import com.devpath.dto.user.request.UpdateTrailStatusRequest
import com.devpath.dto.user.request.UpdateUserRequest
import com.devpath.dto.user.response.DeleteUserResponse
import com.devpath.dto.user.response.DeleteUserTrailResponse
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
    fun create(@RequestBody createUserRequest: CreateUserRequest): UserDTO {
        return userService.create(createUserRequest).toUserDTO()
    }

    @GetMapping("/{email}/{password}")
    fun login(
        @PathVariable email: String,
        @PathVariable password: String,
    ): UserDTO {
        return userService.login(email, password).toUserDTO()
    }

    @GetMapping("/{email}")
    fun read(@PathVariable email: String): UserDTO {
        return userService.read(email).toUserDTO()
    }

    @PatchMapping("/update")
    fun update(@RequestBody updateUserRequest: UpdateUserRequest): UserDTO {
        return userService.update(updateUserRequest).toUserDTO()
    }

    @DeleteMapping("/delete/{email}")
    fun delete(@PathVariable email: String): DeleteUserResponse {
        return userService.delete(email)
    }

    @PostMapping("/{userEmail}/add-trail/{trailId}")
    fun addTrail(
        @PathVariable userEmail: String,
        @PathVariable trailId: Int
    ): UserDTO {
        return userService.addTrail(userEmail, trailId).toUserDTO()
    }

    @PatchMapping("/update-trail")
    fun updateTrailStatus(@RequestBody updateTrailStatusRequest: UpdateTrailStatusRequest): UserDTO {
        return userService.updateTrailStatus(updateTrailStatusRequest).toUserDTO()
    }

    @DeleteMapping("/{userEmail}/delete-trail/{trailId}")
    fun deleteTrail(
        @PathVariable userEmail: String,
        @PathVariable trailId: Int
    ): DeleteUserTrailResponse {
        return userService.deleteTrail(userEmail, trailId)
    }
}
