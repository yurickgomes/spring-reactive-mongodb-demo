package com.example.mongoreactivedemo.projectreactor

import com.example.mongoreactivedemo.common.ExampleDto
import com.example.mongoreactivedemo.common.toDto
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.oshai.kotlinlogging.KotlinLogging
import org.bson.types.ObjectId
import org.springframework.core.io.ResourceLoader
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/reactive")
class ExampleReactiveController(
    private val exampleReactiveRepository: ExampleReactiveRepository,
    private val resourceLoader: ResourceLoader,
) {
    @GetMapping("/find")
    fun find(@RequestParam ids: List<String>): Mono<List<ExampleDto>> {
        return exampleReactiveRepository
            .findAllById(ids.map { ObjectId(it) })
            .map { it.toDto() }
            .collectList()
    }

    @GetMapping("/find-compound-indexes")
    fun findByCompoundIndexes(@RequestParam fileName: String): Mono<List<ExampleDto>> {
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
            logger.info { "File $fileName not found" }
            return Mono.empty()
        }
    }

    companion object {
        private val logger = KotlinLogging.logger { }
        private val objectMapper = jacksonObjectMapper()
    }
}
