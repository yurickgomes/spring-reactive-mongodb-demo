package com.example.mongoreactivedemo

import com.mongodb.WriteConcern
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory
import org.springframework.data.mongodb.core.ReactiveMongoTemplate

@Configuration
class MongoConfig {
    @Bean
    @Qualifier("reactiveBulkMongoTemplate")
    fun reactiveBulkMongoTemplate(reactiveMongoDatabaseFactory: ReactiveMongoDatabaseFactory): ReactiveMongoTemplate {
        val template = ReactiveMongoTemplate(reactiveMongoDatabaseFactory)
        template.setWriteConcern(WriteConcern.W1)
        return template
    }

    @Bean
    @Qualifier("reactiveMongoTemplate")
    fun reactiveMongoTemplate(reactiveMongoDatabaseFactory: ReactiveMongoDatabaseFactory): ReactiveMongoTemplate {
        return ReactiveMongoTemplate(reactiveMongoDatabaseFactory)
    }
}
