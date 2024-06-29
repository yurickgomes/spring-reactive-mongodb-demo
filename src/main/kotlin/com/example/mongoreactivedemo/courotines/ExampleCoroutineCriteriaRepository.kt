package com.example.mongoreactivedemo.courotines

import com.example.mongoreactivedemo.common.Example
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

interface ExampleCoroutineCriteriaRepository {
    fun fetchChunk(pageable: Pageable): Flow<Example>
}
