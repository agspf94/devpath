package com.devpath.controller

import com.devpath.service.HomeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class HomeController(
    private val homeService: HomeService
) {
    @GetMapping
    fun welcome(): String {
        return homeService.welcome()
    }
}
