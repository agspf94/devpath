package com.devpath.controller

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = RANDOM_PORT)
class HomeControllerTest {
    @Autowired
    private lateinit var homeController: HomeController

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @LocalServerPort
    private val port = 0

    @Test
    fun `Home controller should not be null`() {
        assertNotNull(homeController)
    }

    @Test
    fun `Home controller should redirect to swagger home page`() {
        Assertions.assertThat(testRestTemplate.getForObject("http://localhost:$port", String::class.java)).contains("Swagger UI")
    }
}
