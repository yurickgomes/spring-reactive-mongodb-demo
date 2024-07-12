package com.example.mongoreactivedemo.reactor

import com.example.mongoreactivedemo.common.ExampleDto
import com.example.mongoreactivedemo.common.PatchBodyDto
import com.example.mongoreactivedemo.common.toDto
import com.example.mongoreactivedemo.common.toEntity
import org.bson.types.ObjectId
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/reactive/examples")
class ExampleReactiveController(
    private val exampleReactiveRepository: ExampleReactiveRepository,
) {
    @GetMapping
    fun find(@RequestParam page: Int, @RequestParam pageSize: Int): Flux<ExampleDto> {
        val pageable = PageRequest.of(page, pageSize)
        return exampleReactiveRepository
            .fetchChunk(pageable)
            .map { it.toDto() }
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): Mono<ExampleDto> {
        return exampleReactiveRepository.findById(ObjectId(id)).map { it.toDto() }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody body: ExampleDto): Mono<ExampleDto> {
        val persisted = exampleReactiveRepository.save(body.toEntity())
        return persisted.map { it.toDto() }
    }

    @DeleteMapping("/{id}")
    fun remove(@PathVariable id: String): Mono<Void> {
        return exampleReactiveRepository.deleteById(ObjectId(id))
    }

    @PatchMapping("/{id}")
    fun updateDescription(@PathVariable id: String, @RequestBody body: PatchBodyDto): Mono<ExampleDto> {
        val persisted = exampleReactiveRepository.updateDescription(ObjectId(id), body)
        return persisted.map { it.toDto() }
    }
}
