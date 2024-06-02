package com.example.mongoreactivedemo

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
class ExampleCriteriaRepositoryImpl(
    @Qualifier("reactiveMongoTemplate")
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
) : ExampleCriteriaRepository {
    override fun findByCompoundIndexesList(compounds: List<CompoundIndexBodyDto>): Flux<Example> {
        val criteria = Criteria()
        val orCriteria = mutableListOf<Criteria>()
        for (compound in compounds) {
            val andCriteria = Criteria.where("fieldA").`is`(compound.fieldA)
                .and("fieldB").`is`(compound.fieldB)
            orCriteria.add(andCriteria)
        }
        criteria.orOperator(orCriteria)
        return reactiveMongoTemplate.find(Query(criteria), Example::class.java)
    }
}
