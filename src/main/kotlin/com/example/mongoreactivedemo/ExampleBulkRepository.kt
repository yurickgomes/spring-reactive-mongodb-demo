package com.example.mongoreactivedemo

import com.mongodb.MongoBulkWriteException
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.mongodb.core.BulkOperations
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ExampleBulkRepository(
    @Qualifier("reactiveBulkMongoTemplate")
    private val reactiveBulkMongoTemplate: ReactiveMongoTemplate,
) {
    suspend fun reactiveBulkInsert(totalAccounts: Long) {
        try {
            val bulkOps = reactiveBulkMongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Example::class.java)
            val valuesToInsert = mutableListOf<Example>()
            for (i in 0 until totalAccounts) {
                val randomValue = UUID.randomUUID().toString()
                val example = Example(
                    indexedField = randomValue,
                    description = randomValue,
                    fieldA = randomValue,
                    fieldB = randomValue,
                )
                valuesToInsert.add(example)
            }
            val bulkWriteResultMono = bulkOps.insert(valuesToInsert).execute()
            val bulkWriteResult = bulkWriteResultMono.awaitFirst()
            logger.info { "Inserted count = ${bulkWriteResult.insertedCount}" }
        } catch (e: Throwable) {
            if (e is MongoBulkWriteException) {
                logger.info { "Inserted count = ${e.writeResult.insertedCount}" }
                e.writeErrors.forEach { error ->
                    logger.error { "Error message = ${error.message}" }
                }
            } else if (e.cause is MongoBulkWriteException) {
                val cause = e.cause as MongoBulkWriteException
                logger.info { "Inserted count = ${cause.writeResult.insertedCount}" }
                cause.writeErrors.forEach { error ->
                    logger.error { "Error message = ${error.message}" }
                }
            } else {
                throw e
            }
        }
    }

    suspend fun reactiveBulkUpdate(updateBodyDto: UpdateBodyDto) {
        try {
            val bulkOps = reactiveBulkMongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Example::class.java)
            updateBodyDto.accounts.forEach {
                bulkOps.updateMulti(
                    Query.query(Criteria.where("indexedField").`is`(it.indexedField)),
                    Update().set("indexedField", it.newIndexedField).set("description", it.description),
                )
            }
            val bulkWriteResultMono = bulkOps.execute()
            val bulkWriteResult = bulkWriteResultMono.awaitFirst()
            logger.info { "Modified count = ${bulkWriteResult.modifiedCount}" }
        } catch (e: Throwable) {
            if (e is MongoBulkWriteException) {
                logger.info { "Modified count = ${e.writeResult.modifiedCount}" }
                e.writeErrors.forEach { error ->
                    logger.error { "Error message = ${error.message}" }
                }
            } else if (e.cause is MongoBulkWriteException) {
                val cause = e.cause as MongoBulkWriteException
                logger.info { "Modified count = ${cause.writeResult.modifiedCount}" }
                cause.writeErrors.forEach { error ->
                    logger.error { "Error message = ${error.message}" }
                }
            } else {
                throw e
            }
        }
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}
