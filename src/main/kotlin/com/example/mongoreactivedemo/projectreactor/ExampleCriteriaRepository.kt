package com.example.mongoreactivedemo.projectreactor

import com.example.mongoreactivedemo.common.Example
import reactor.core.publisher.Flux

interface ExampleCriteriaRepository {
    fun findByCompoundIndexesList(compounds: List<CompoundIndexBodyDto>): Flux<Example>
}
