package com.wiczha.musiquackkotlin.user.service

import com.wiczha.musiquackkotlin.user.controller.request.UserCreateRequest
import com.wiczha.musiquackkotlin.user.controller.request.UserUpdateRequest
import com.wiczha.musiquackkotlin.user.controller.response.UserCreateResponse
import com.wiczha.musiquackkotlin.user.controller.response.UserUpdateResponse
import com.wiczha.musiquackkotlin.user.domain.User

interface UserService {
    fun create(request: UserCreateRequest): UserCreateResponse
    fun update(request: UserUpdateRequest): UserUpdateResponse
    fun findBySessionId(userId: String): User
}