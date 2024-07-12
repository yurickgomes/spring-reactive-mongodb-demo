package com.example.mongoreactivedemo.courotines

import com.example.mongoreactivedemo.common.Example
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.Update
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ExampleCoroutineRepository : ExampleCoroutineCriteriaRepository, CoroutineCrudRepository<Example, ObjectId> {
    suspend fun findByIndexedField(indexedField: String): Example?

    suspend fun deleteByIndexedField(indexedField: String)

    @Update("{ '\$set': { 'description': ?0 } }")
    @Query("{ 'indexedField': ?1 }")
    suspend fun updateDescriptionByIndexedField(description: String, indexedField: String): Long
}
