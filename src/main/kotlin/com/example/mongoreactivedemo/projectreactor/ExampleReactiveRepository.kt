package com.example.mongoreactivedemo.projectreactor

import com.example.mongoreactivedemo.common.Example
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface ExampleReactiveRepository : ReactiveMongoRepository<Example, ObjectId>, ExampleCriteriaRepository
