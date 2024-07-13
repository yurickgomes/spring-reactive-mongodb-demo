package com.example.mongoreactivedemo.common

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class CompoundIndexDto(
    val fieldA: String,
    val fieldB: String,
)
