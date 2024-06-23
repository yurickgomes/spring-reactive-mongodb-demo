package com.example.mongoreactivedemo.reactor

import com.example.mongoreactivedemo.common.Example
import reactor.core.publisher.Flux

interface ExampleCriteriaRepository {
    fun findByCompoundIndexesList(compounds: List<CompoundIndexDto>): Flux<Example>
}