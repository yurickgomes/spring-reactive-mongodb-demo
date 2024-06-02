package com.example.mongoreactivedemo

import reactor.core.publisher.Flux

interface ExampleCriteriaRepository {
    fun findByCompoundIndexesList(compounds: List<CompoundIndexBodyDto>): Flux<Example>
}
