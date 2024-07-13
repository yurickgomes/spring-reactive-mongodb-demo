package com.example.mongoreactivedemo.reactor

import com.example.mongoreactivedemo.common.CompoundIndexDto
import com.example.mongoreactivedemo.common.Example
import com.example.mongoreactivedemo.common.PatchBodyDto
import org.bson.types.ObjectId
import org.springframework.data.domain.Pageable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ExampleCriteriaRepository {
    fun findByCompoundIndexesList(compounds: List<CompoundIndexDto>): Flux<Example>
    fun fetchChunk(pageable: Pageable): Flux<Example>
    fun updateDescription(id: ObjectId, description: PatchBodyDto): Mono<Example>
}
