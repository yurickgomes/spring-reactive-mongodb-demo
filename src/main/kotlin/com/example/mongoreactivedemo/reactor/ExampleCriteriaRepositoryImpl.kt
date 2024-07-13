package com.example.mongoreactivedemo.reactor

import com.example.mongoreactivedemo.common.CompoundIndexDto
import com.example.mongoreactivedemo.common.Example
import com.example.mongoreactivedemo.common.PatchBodyDto
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class ExampleCriteriaRepositoryImpl(
    @Qualifier("reactiveMongoTemplate")
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
) : ExampleCriteriaRepository {
    override fun findByCompoundIndexesList(compounds: List<CompoundIndexDto>): Flux<Example> {
        val criteria = Criteria()
        val orCriteria = mutableListOf<Criteria>()
        for (compound in compounds) {
            val andCriteria = Criteria.where("fieldA").`is`(compound.fieldA).and("fieldB").`is`(compound.fieldB)
            orCriteria.add(andCriteria)
        }
        criteria.orOperator(orCriteria)
        return reactiveMongoTemplate.find(Query(criteria), Example::class.java)
    }

    override fun fetchChunk(pageable: Pageable): Flux<Example> {
        return reactiveMongoTemplate.find(Query().with(pageable), Example::class.java)
    }

    override fun updateDescription(id: ObjectId, description: PatchBodyDto): Mono<Example> {
        return reactiveMongoTemplate.findAndModify(
            Query.query(Criteria.where("_id").`is`(id)),
            Update().set("description", description.description),
            FindAndModifyOptions().returnNew(true),
            Example::class.java,
        )
    }
}
