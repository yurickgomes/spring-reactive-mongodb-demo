package com.example.mongoreactivedemo.reactor

import com.example.mongoreactivedemo.common.ExampleDto
import com.example.mongoreactivedemo.common.toDto
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.bson.types.ObjectId
import org.springframework.core.io.ResourceLoader
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
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

    @GetMapping("/find-by-criteria")
    fun findByCriteria(@RequestParam fileName: String): Mono<List<ExampleDto>> {
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

    @GetMapping("/find-by-criteria-2")
    fun findByCriteriaV2(@RequestParam fileName: String): Mono<List<ExampleDto>> {
        val resource = resourceLoader.getResource("classpath:$fileName")
        val sourceFlux: Flux<CompoundIndexDto> = if (resource.exists()) {
            Flux.create { sink ->
                try {
                    resource.inputStream.use { inputStream ->
                        inputStream.bufferedReader().use { reader ->
                            reader.forEachLine { line ->
                                val compoundIndex = objectMapper.readValue<CompoundIndexDto>(line)
                                sink.next(compoundIndex)
                            }
                        }
                    }
                    sink.complete()
                } catch (e: Throwable) {
                    sink.error(e)
                }
            }
        } else {
            Flux.error(NoSuchElementException("File $fileName not found"))
        }

        return sourceFlux
            .flatMap { exampleReactiveRepository.findByCompoundIndex(it) }
            .map { it.toDto() }
            .collectList()
    }

    companion object {
        private val objectMapper = jacksonObjectMapper()
    }
}
