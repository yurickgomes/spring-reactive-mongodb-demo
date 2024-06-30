package com.example.mongoreactivedemo.courotines

import com.example.mongoreactivedemo.common.ExampleDto
import com.example.mongoreactivedemo.common.toDto
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v2/coroutine/examples")
class ExampleCoroutineControllerV2(
    private val exampleCoroutineRepository: ExampleCoroutineRepository,
) {
    @GetMapping("/{indexedField}")
    suspend fun findById(@PathVariable indexedField: String): ExampleDto? {
        val persistedExample = exampleCoroutineRepository.findByIndexedField(indexedField)
        return persistedExample?.toDto()
    }

    @DeleteMapping("/{indexedField}")
    suspend fun remove(@PathVariable indexedField: String) {
        return exampleCoroutineRepository.deleteByIndexedField(indexedField)
    }
}
