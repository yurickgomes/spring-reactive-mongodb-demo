package com.example.mongoreactivedemo

import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ExampleCoroutineRepository : CoroutineCrudRepository<Example, ObjectId>
