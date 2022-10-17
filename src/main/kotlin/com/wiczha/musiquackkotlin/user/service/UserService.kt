package com.wiczha.musiquackkotlin.user.service

import com.wiczha.musiquackkotlin.user.controller.request.UserCreateRequest
import com.wiczha.musiquackkotlin.user.controller.response.UserCreateResponse
import com.wiczha.musiquackkotlin.user.domain.User

interface UserService {
    fun create(request: UserCreateRequest): UserCreateResponse
    fun findByUserId(userId: String): User
    fun findByUsername(username: String): User
    fun findAllUsers(): List<User>
}