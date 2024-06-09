package com.example.mongoreactivedemo.projectreactor

import com.example.mongoreactivedemo.common.ExampleDto
import com.example.mongoreactivedemo.common.toDto
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/reactive")
class ExampleReactiveController(
    private val exampleReactiveRepository: ExampleReactiveRepository,
) {
    @GetMapping("/find")
    fun find(@RequestParam ids: List<String>): Mono<List<ExampleDto>> {
        return exampleReactiveRepository
            .findAllById(ids.map { ObjectId(it) })
            .map { it.toDto() }
            .collectList()
    }

    @GetMapping("/find-compound-indexes")
    fun findByCompoundIndexes(): Mono<List<ExampleDto>> {
        val compounds = emptyList<CompoundIndexBodyDto>()
        return exampleReactiveRepository.findByCompoundIndexesList(compounds)
            .map { it.toDto() }
            .collectList()
    }
}
