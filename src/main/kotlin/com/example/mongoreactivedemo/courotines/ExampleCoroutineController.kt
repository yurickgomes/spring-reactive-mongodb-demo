package com.example.mongoreactivedemo.courotines

import com.example.mongoreactivedemo.common.ExampleDto
import com.example.mongoreactivedemo.common.toDto
import com.example.mongoreactivedemo.common.toEntity
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/coroutine/examples")
class ExampleCoroutineController(
    private val exampleCoroutineRepository: ExampleCoroutineRepository,
) {
    @GetMapping
    suspend fun find(@RequestParam page: Int, @RequestParam pageSize: Int): List<ExampleDto> {
        val result = mutableListOf<ExampleDto>()
        val pageable = PageRequest.of(page, pageSize)
        exampleCoroutineRepository
            .fetchChunk(pageable)
            .map { it.toDto() }
            .toList(result)
        return result
    }

    @GetMapping("/{id}")
    suspend fun findById(@PathVariable id: String): ExampleDto? {
        val persistedExample = exampleCoroutineRepository.findById(ObjectId(id))
        return persistedExample?.toDto()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun create(@RequestBody body: ExampleDto): ExampleDto {
        val persisted = exampleCoroutineRepository.save(body.toEntity())
        return persisted.toDto()
    }

    @DeleteMapping("/{id}")
    suspend fun remove(@PathVariable id: String) {
        return exampleCoroutineRepository.deleteById(ObjectId(id))
    }
}
