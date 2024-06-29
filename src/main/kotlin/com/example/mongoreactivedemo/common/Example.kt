package com.example.mongoreactivedemo.common

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

fun ExampleDto.toEntity() = Example(
    indexedField = indexedField,
    description = description,
    fieldA = fieldA,
    fieldB = fieldB,
)

