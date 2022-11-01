package com.wiczha.musiquackkotlin.user.controller

import com.wiczha.musiquackkotlin.user.service.HealthService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheck {

    @GetMapping("/health")
    fun healthCheck(): HttpStatus = createHealthService().getHealth()

    fun createHealthService(): HealthService = HealthService()
}