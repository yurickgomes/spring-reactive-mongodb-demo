package com.example.mongoreactivedemo.courotines

import com.example.mongoreactivedemo.common.ExampleDto
import com.example.mongoreactivedemo.common.toDto
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
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
    suspend fun find(@RequestParam ids: List<String>): List<ExampleDto> {
        val result = mutableListOf<ExampleDto>()
        exampleCoroutineRepository
            .findAllById(ids.asFlow().map { ObjectId(it) })
            .map { it.toDto() }
            .toList(result)
        return result
    }
}
