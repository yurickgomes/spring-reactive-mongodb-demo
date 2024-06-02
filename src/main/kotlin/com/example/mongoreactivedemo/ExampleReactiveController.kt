package com.example.mongoreactivedemo

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/reactive")
class ExampleReactiveController(
    private val exampleReactiveRepository: ExampleReactiveRepository,
) {
    @GetMapping("/find")
    fun find(): Mono<List<ExampleDto>> {
        val compounds = listOf(
            CompoundIndexBodyDto(
                fieldA = "a7320bc4-3c90-476f-9677-a0b1153d173a",
                fieldB = "a7320bc4-3c90-476f-9677-a0b1153d173a"
            ),
            CompoundIndexBodyDto(
                fieldA = "1c483b1f-7205-4430-8e1f-d443b25a0d95",
                fieldB = "1c483b1f-7205-4430-8e1f-d443b25a0d95"
            ),
            CompoundIndexBodyDto(
                fieldA = "a712b881-f3ae-4d78-a6f8-0fea6a35e35d",
                fieldB = "a712b881-f3ae-4d78-a6f8-0fea6a35e35d"
            ),
            CompoundIndexBodyDto(
                fieldA = "b5e02421-6607-4d0e-ab79-da65d758d97b",
                fieldB = "b5e02421-6607-4d0e-ab79-da65d758d97b"
            ),
            CompoundIndexBodyDto(
                fieldA = "7a36004d-a7a9-4ee5-bf00-6d6ac8cd8eec",
                fieldB = "7a36004d-a7a9-4ee5-bf00-6d6ac8cd8eec"
            ),
            CompoundIndexBodyDto(
                fieldA = "bc123807-0adc-404b-bdc6-61b19a855f04",
                fieldB = "bc123807-0adc-404b-bdc6-61b19a855f04"
            ),
            CompoundIndexBodyDto(
                fieldA = "bc123807-0adc-404b-bdc6-61b19a855f0488",
                fieldB = "bc123807-0adc-404b-bdc6-61b19a855f0488"
            ),
        )
        return exampleReactiveRepository.findByCompoundIndexesList(compounds)
            .map { it.toDto() }
            .collectList()
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}
