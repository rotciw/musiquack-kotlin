package com.wiczha.musiquackkotlin.config

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.DefaultTableNameResolver
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverterFactory
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*


@Configuration
@EnableDynamoDBRepositories(dynamoDBMapperConfigRef = "dynamoDBMapperConfig", basePackages = ["com.wiczha.musiquackkotlin.user.domain"])
class DynamoDBConfig {

    @Value("\${dynamodb.table.name}") var dynamoDBTableName: String = ""
    companion object {
        class LocalDateTimeConverter : DynamoDBTypeConverter<Date, LocalDateTime> {
            override fun convert(source: LocalDateTime): Date {
                return Date.from(source.toInstant(ZoneOffset.UTC))
            }

            override fun unconvert(source: Date): LocalDateTime {
                return source.toInstant().atZone(TimeZone.getDefault().toZoneId()).toLocalDateTime()
            }
        }
    }

    @Bean
    fun dynamoDBMapperConfig(): DynamoDBMapperConfig  {
        // Create empty DynamoDBMapperConfig builder
        val builder = DynamoDBMapperConfig.Builder()
        // Inject missing defaults from the deprecated method
        builder.withTypeConverterFactory(DynamoDBTypeConverterFactory.standard())
        builder.withTableNameResolver(DefaultTableNameResolver.INSTANCE)
        // Inject the table name overrider bean
        builder.withTableNameOverride(tableNameOverrider())
        return builder.build()
    }

//    @Primary
//    @Bean
//    fun dynamoDBMapper(amazonDynamoDB: AmazonDynamoDB): DynamoDBMapper {
//        return DynamoDBMapper(amazonDynamoDB, dynamoDBMapperConfig())
//    }

    @Bean
    fun amazonDynamoDB(): AmazonDynamoDB {
        return AmazonDynamoDBClientBuilder.standard()
            .build()
    }

    @Bean
    fun tableNameOverrider(): DynamoDBMapperConfig.TableNameOverride {
        return DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement(dynamoDBTableName);
    }
}