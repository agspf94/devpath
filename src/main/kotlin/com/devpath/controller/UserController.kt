package com.devpath.controller

import com.devpath.dto.user.request.CreateUserRequest
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
    fun createUser(@RequestBody createUserRequest: CreateUserRequest): User {
        return userService.createUser(createUserRequest)
    }

    @GetMapping("/{email}")
    fun readUser(@PathVariable email: String): User {
        return userService.readUser(email)
    }

    @PatchMapping("/update")
    fun updateUser(@RequestBody updateUserRequest: UpdateUserRequest): User {
        return userService.updateUser(updateUserRequest)
    }

    @DeleteMapping("/delete/{email}")
    fun deleteUser(@PathVariable email: String): DeleteUserResponse {
        return userService.deleteUser(email)
    }

    @PostMapping("{userEmail}/add-trail/{trailId}")
    fun addTrail(
        @PathVariable userEmail: String,
        @PathVariable trailId: Int
    ): User {
        return userService.addTrail(userEmail, trailId)
    }
}
