package com.wiczha.musiquackkotlin.user.domain

import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.data.repository.CrudRepository

@EnableScan
interface UserRepository : CrudRepository<User, String> {
    fun findByUserIdOrderByCreatedAtAsc(userId: String): User
    fun findByUsernameOrderByCreatedAtDesc(userId: String): User
}
