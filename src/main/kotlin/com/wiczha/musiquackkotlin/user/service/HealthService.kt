package com.wiczha.musiquackkotlin.user.service

import org.springframework.http.HttpStatus

class HealthService {

    fun getHealth(): HttpStatus {
        return HttpStatus.OK
    }
}