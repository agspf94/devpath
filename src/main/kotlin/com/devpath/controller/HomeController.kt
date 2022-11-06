package com.devpath.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/")
class HomeController {
    @GetMapping
    fun welcome(httpServletResponse: HttpServletResponse) {
        return httpServletResponse.sendRedirect("/swagger-ui.html")
    }
}
