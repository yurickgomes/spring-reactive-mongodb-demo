package com.example.mongoreactivedemo.bulk

import com.example.mongoreactivedemo.common.CompoundIndexDto
import com.example.mongoreactivedemo.common.ExampleDto
import com.example.mongoreactivedemo.common.toDto
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
import reactor.core.publisher.Flux

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
    fun insert(@RequestParam totalAccounts: Long) {
        exampleBulkRepository.insert(totalAccounts)
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun update(@RequestBody updateBulkBodyDto: UpdateBulkBodyDto) {
        exampleBulkRepository.update(updateBulkBodyDto)
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun remove(@RequestBody deleteBulkBodyDto: DeleteBulkBodyDto) {
        exampleBulkRepository.remove(deleteBulkBodyDto)
    }

    @GetMapping("/file")
    fun loadFromFile(@RequestParam fileName: String): Flux<ExampleDto> {
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
        } else {
            return Flux.error(NoSuchElementException("File $fileName not found"))
        }
    }
}
