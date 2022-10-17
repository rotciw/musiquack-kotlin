package com.wiczha.musiquackkotlin.user.controller.request

data class UserCreateRequest (
    val userId: String,
    val username: String,
    val accessToken: String,
    val refreshToken: String,
)