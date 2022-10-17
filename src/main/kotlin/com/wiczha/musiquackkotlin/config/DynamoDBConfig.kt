package com.wiczha.musiquackkotlin.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Configuration
@EnableDynamoDBRepositories(basePackages = ["com.wiczha.musiquackkotlin.user.domain"])
class DynamoDBConfig(
    @Value("\${amazon.dynamodb.endpoint}") private val endpoint: String,
    @Value("\${amazon.aws.accessKey}") private val accessKey: String,
    @Value("\${amazon.aws.secretKey}") private val secretKey: String,
    @Value("\${amazon.aws.region}") private val region: String
) {
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


    @Primary
    @Bean
    fun dynamoDBMapper(amazonDynamoDB: AmazonDynamoDB): DynamoDBMapper {
        return DynamoDBMapper(amazonDynamoDB, DynamoDBMapperConfig.DEFAULT)
    }

    @Bean
    fun amazonDynamoDB(): AmazonDynamoDB {
        val awsCredentials = BasicAWSCredentials(accessKey, secretKey)
        val awsCredentialsProvider = AWSStaticCredentialsProvider(awsCredentials)
        val endpointConfiguration = AwsClientBuilder.EndpointConfiguration(endpoint, region)
        return AmazonDynamoDBClientBuilder.standard()
            .withCredentials(awsCredentialsProvider)
            .withEndpointConfiguration(endpointConfiguration)
            .build()
    }

    @Bean
    fun awsCredentials() = BasicAWSCredentials(accessKey, secretKey)
}