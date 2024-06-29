package com.example.mongoreactivedemo.reactor

import com.example.mongoreactivedemo.common.ExampleDto
import com.example.mongoreactivedemo.common.toDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.core.io.ResourceLoader
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/read-file/examples")
class ExampleInputFromFileController(
    private val exampleReactiveRepository: ExampleReactiveRepository,
    private val resourceLoader: ResourceLoader,
    private val objectMapper: ObjectMapper
) {
    @GetMapping("/file1")
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

    @GetMapping("/file2")
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
}
