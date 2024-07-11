package com.example.mongoreactivedemo.courotines

import com.example.mongoreactivedemo.common.ExampleDto
import com.example.mongoreactivedemo.common.PatchBodyDto
import com.example.mongoreactivedemo.common.toDto
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
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

    @PatchMapping("/{indexedField}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    suspend fun updateDescription(
        @PathVariable indexedField: String,
        @RequestBody body: PatchBodyDto,
    ) {
        val updatedCount = exampleCoroutineRepository.updateDescriptionByIndexedField(body.description, indexedField)
        logger.info { "Documents update count = $updatedCount" }
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}
