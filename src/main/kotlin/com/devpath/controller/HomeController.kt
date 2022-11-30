package com.devpath.controller

import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class HomeController {
    @GetMapping
    fun welcome(httpServletResponse: HttpServletResponse) {
        return httpServletResponse.sendRedirect("/swagger-ui.html")
    }
}
