package com.example.mongoreactivedemo.bulk

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/bulk/examples")
class ExampleBulkController(
    private val exampleBulkRepository: ExampleBulkRepository,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun create(@RequestParam totalAccounts: Long) {
        exampleBulkRepository.reactiveBulkInsert(totalAccounts)
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun update(@RequestBody updateBulkBodyDto: UpdateBulkBodyDto) {
        exampleBulkRepository.reactiveBulkUpdate(updateBulkBodyDto)
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun delete(@RequestBody deleteBulkBodyDto: DeleteBulkBodyDto) {
        exampleBulkRepository.reactiveBulkDelete(deleteBulkBodyDto)
    }
}
