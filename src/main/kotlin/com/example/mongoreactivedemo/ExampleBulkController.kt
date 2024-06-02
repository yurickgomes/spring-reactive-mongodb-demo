package com.example.mongoreactivedemo

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/bulk")
class ExampleBulkController(
    private val exampleBulkRepository: ExampleBulkRepository,
) {
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun create(@RequestParam totalAccounts: Long) {
        exampleBulkRepository.reactiveBulkInsert(totalAccounts)
    }

    @PatchMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun update(@RequestBody updateBodyDto: UpdateBodyDto) {
        exampleBulkRepository.reactiveBulkUpdate(updateBodyDto)
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}
