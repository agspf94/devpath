package com.devpath

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DevPathApplication

fun main(args: Array<String>) {
	runApplication<DevPathApplication>(*args)
}
