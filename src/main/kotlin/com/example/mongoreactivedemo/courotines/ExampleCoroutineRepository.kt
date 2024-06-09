package com.example.mongoreactivedemo.courotines

import com.example.mongoreactivedemo.common.Example
import org.bson.types.ObjectId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ExampleCoroutineRepository : CoroutineCrudRepository<Example, ObjectId>
