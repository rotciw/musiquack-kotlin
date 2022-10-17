package com.wiczha.musiquackkotlin.user.domain

import com.amazonaws.services.dynamodbv2.datamodeling.*
import com.wiczha.musiquackkotlin.config.DynamoDBConfig
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.util.*

@DynamoDBTable(tableName = "musiquack_users")
class User(
    @field:DynamoDBHashKey
    @field:DynamoDBAttribute(attributeName = "userId")
    var userId: String = UUID.randomUUID().toString(),

    @field:DynamoDBAttribute(attributeName = "username")
    var username: String = "",

    @field:DynamoDBAttribute(attributeName = "accessToken")
    var accessToken: String = "",

    @field:DynamoDBAttribute(attributeName = "refreshToken")
    var refreshToken: String = "",

    @field:DynamoDBAttribute(attributeName = "createdAt")
    @field:DynamoDBTypeConverted(converter = DynamoDBConfig.Companion.LocalDateTimeConverter::class)
    var createdAt: LocalDateTime = now()
)