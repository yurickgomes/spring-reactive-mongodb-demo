package com.example.mongoreactivedemo

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId

@Document
data class Example(
    @MongoId
    val id: ObjectId? = null,
    val indexedField: String,
    val description: String,
    val fieldA: String,
    val fieldB: String,
)
