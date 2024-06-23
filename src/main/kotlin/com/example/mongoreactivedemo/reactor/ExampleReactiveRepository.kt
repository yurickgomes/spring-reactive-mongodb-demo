package com.example.mongoreactivedemo.reactor

import com.example.mongoreactivedemo.common.Example
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface ExampleReactiveRepository : ReactiveMongoRepository<Example, ObjectId>, ExampleCriteriaRepository {
    @Query("{'fieldA': ?0.fieldA, 'fieldB': ?0.fieldB}")
    fun findByCompoundIndex(compoundIndexDto: CompoundIndexDto): Mono<Example>
}
