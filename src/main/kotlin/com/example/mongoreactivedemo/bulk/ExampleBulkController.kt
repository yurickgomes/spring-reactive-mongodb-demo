package com.example.mongoreactivedemo.bulk

import com.example.mongoreactivedemo.common.ExampleDto
import com.example.mongoreactivedemo.common.toDto
import com.example.mongoreactivedemo.reactor.CompoundIndexDto
import com.example.mongoreactivedemo.reactor.ExampleReactiveRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.core.io.ResourceLoader
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/bulk/examples")
class ExampleBulkController(
    private val exampleBulkRepository: ExampleBulkRepository,
    private val exampleReactiveRepository: ExampleReactiveRepository,
    private val resourceLoader: ResourceLoader,
    private val objectMapper: ObjectMapper,
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

    @GetMapping("/file")
    fun findAllFromFile(@RequestParam fileName: String): Mono<List<ExampleDto>> {
        val resource = resourceLoader.getResource("classpath:$fileName")
        val compounds = mutableListOf<CompoundIndexDto>()
        if (resource.exists()) {
            resource.inputStream.use { inputStream ->
                inputStream.bufferedReader().use { reader ->
                    reader.forEachLine { line ->
                        val compoundIndex = objectMapper.readValue<CompoundIndexDto>(line)
                        compounds.add(compoundIndex)
                    }
                }
            }

            return exampleReactiveRepository.findByCompoundIndexesList(compounds)
                .map { it.toDto() }
                .collectList()
        } else {
            return Mono.error(NoSuchElementException("File $fileName not found"))
        }
    }
}
