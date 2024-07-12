package com.example.mongoreactivedemo.bulk

import com.example.mongoreactivedemo.common.ExampleDto
import com.example.mongoreactivedemo.common.toDto
import com.example.mongoreactivedemo.reactor.CompoundIndexDto
import com.example.mongoreactivedemo.reactor.ExampleReactiveRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.core.io.ResourceLoader
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/v2/bulk/examples")
class ExampleBulkControllerV2(
    private val exampleReactiveRepository: ExampleReactiveRepository,
    private val resourceLoader: ResourceLoader,
    private val objectMapper: ObjectMapper,
) {

    @GetMapping("/file")
    fun loadFromFile(@RequestParam fileName: String): Flux<ExampleDto> {
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
    }
}
