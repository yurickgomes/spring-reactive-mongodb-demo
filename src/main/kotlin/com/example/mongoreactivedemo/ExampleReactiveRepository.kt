package com.example.mongoreactivedemo

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface ExampleReactiveRepository : ReactiveMongoRepository<Example, ObjectId>, ExampleCriteriaRepository
