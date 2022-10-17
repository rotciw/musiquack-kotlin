package com.wiczha.musiquackkotlin.user.service.impl

import com.wiczha.musiquackkotlin.user.controller.request.UserCreateRequest
import com.wiczha.musiquackkotlin.user.controller.response.UserCreateResponse
import com.wiczha.musiquackkotlin.user.domain.User
import com.wiczha.musiquackkotlin.user.domain.UserRepository
import com.wiczha.musiquackkotlin.user.service.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {
    override fun create(request: UserCreateRequest): UserCreateResponse {
        val user = userRepository.save(
            User(
                userId = request.userId,
                username = request.username,
                accessToken = request.accessToken,
                refreshToken = request.refreshToken,
            )
        )
        return UserCreateResponse.from(user)
    }

    override fun findByUserId(userId: String): User {
        return userRepository.findByUserIdOrderByCreatedAtAsc(userId)
    }


    override fun findByUsername(userId: String): User {
        return userRepository.findByUsernameOrderByCreatedAtDesc(userId)
    }

    override fun findAllUsers(): List<User> {
        return userRepository.findAll() as List<User>
    }

}