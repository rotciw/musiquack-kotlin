package com.wiczha.musiquackkotlin.user.controller.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.wiczha.musiquackkotlin.user.domain.User
import java.time.LocalDateTime

data class UserCreateResponse (
    val userId: String,
    val username: String,
    val accessToken: String,
    val refreshToken: String,
    @field:JsonFormat(pattern="dd-MM-yyyy")
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(user: User) = UserCreateResponse(
            userId = user.userId,
            username = user.username,
            accessToken = user.accessToken,
            refreshToken = user.refreshToken,
            createdAt = user.createdAt
        )
    }
}