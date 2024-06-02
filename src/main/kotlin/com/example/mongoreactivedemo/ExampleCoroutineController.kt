package com.example.mongoreactivedemo

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/coroutine")
class ExampleCoroutineController(
    private val exampleCoroutineRepository: ExampleCoroutineRepository,
) {
    @GetMapping("/find")
    suspend fun find(@RequestParam ids: List<String>): List<String> {
        val result = mutableListOf<String>()
        exampleCoroutineRepository.findAllById(ids.asFlow().map { ObjectId(it) })
            .onEach { logger.info { "onEach -> ${it.indexedField}" } }.map { it.indexedField }.toList(result)
        return result
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}
