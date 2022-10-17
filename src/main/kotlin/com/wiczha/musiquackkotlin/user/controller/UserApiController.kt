package com.wiczha.musiquackkotlin.user.controller

import com.wiczha.musiquackkotlin.user.controller.request.UserCreateRequest
import com.wiczha.musiquackkotlin.user.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/user")
class UserApiController(
    private val userService: UserService
) {
    @PostMapping
    fun create(@RequestBody request: UserCreateRequest) = userService.create(request)

    @GetMapping
    fun index() = userService.findAllUsers()

    @GetMapping("/id/{userId}")
    fun findByUserId(@PathVariable("userId") userId: String) = userService.findByUserId(userId)

    @GetMapping("/name/{username}")
    fun findByUsername(@PathVariable("username") username: String) = userService.findByUsername(username)
}