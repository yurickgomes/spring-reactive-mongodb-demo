package com.example.mongoreactivedemo.courotines

import com.example.mongoreactivedemo.common.Example
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class ExampleCoroutineCriteriaRepositoryImpl(
    @Qualifier("reactiveMongoTemplate")
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
) : ExampleCoroutineCriteriaRepository {
    override fun findPage(pageable: Pageable): Flow<Example> {
        return reactiveMongoTemplate.find(Query().with(pageable), Example::class.java).asFlow()
    }
}
