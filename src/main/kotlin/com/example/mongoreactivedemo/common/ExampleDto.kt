package com.example.mongoreactivedemo.common

data class ExampleDto(
    val indexedField: String,
    val description: String,
    val fieldA: String,
    val fieldB: String,
)

fun Example.toDto() = ExampleDto(
    indexedField = indexedField,
    description = description,
    fieldA = fieldA,
    fieldB = fieldB,
)
