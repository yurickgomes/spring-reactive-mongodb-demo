package com.example.mongoreactivedemo.bulk

import com.example.mongoreactivedemo.common.Example
import com.mongodb.MongoBulkWriteException
import io.github.oshai.kotlinlogging.KotlinLogging
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
    fun reactiveBulkInsert(totalAccounts: Long) {
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
        bulkWriteResultMono
            .subscribe(
                {
                    logger.info { "SUCCESS -> Inserted count = ${it.insertedCount}" }
                },
            ) {
                handleBulkExceptions(it)
            }
    }

    fun reactiveBulkUpdate(updateBodyDto: UpdateBodyDto) {
        val bulkOps = reactiveBulkMongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Example::class.java)
        updateBodyDto.accounts.forEach {
            bulkOps.updateMulti(
                Query.query(Criteria.where("indexedField").`is`(it.indexedField)),
                Update()
                    .set("indexedField", it.newIndexedField)
                    .set("description", it.description),
            )
        }
        val bulkWriteResultMono = bulkOps.execute()
        bulkWriteResultMono
            .subscribe(
                {
                    logger.info { "SUCCESS -> Modified count = ${it.modifiedCount}" }
                },
            ) {
                handleBulkExceptions(it)
            }
    }

    fun reactiveBulkDelete(deleteBodyDto: DeleteBodyDto) {
        val bulkOps = reactiveBulkMongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Example::class.java)
        deleteBodyDto.accounts.forEach {
            bulkOps.remove(Query.query(Criteria.where("indexedField").`is`(it)))
        }
        val bulkWriteResultMono = bulkOps.execute()
        bulkWriteResultMono
            .subscribe(
                {
                    logger.info { "SUCCESS -> Modified count = ${it.deletedCount}" }
                },
            ) {
                handleBulkExceptions(it)
            }
    }

    private fun handleBulkExceptions(e: Throwable) {
        if (e is MongoBulkWriteException) {
            logMongoBulkWriteException(e, messagePrefix = "ROOT EXCEPTION ->")
        } else if (e.cause is MongoBulkWriteException) {
            val cause = e.cause as MongoBulkWriteException
            logMongoBulkWriteException(cause, messagePrefix = "CAUSE EXCEPTION ->")
        } else {
            logger.error(e) { "Bulk operation threw an unexpected error" }
        }
    }

    private fun logMongoBulkWriteException(e: MongoBulkWriteException, messagePrefix: String) {
        logger.info { "$messagePrefix Inserted count = ${e.writeResult.insertedCount}" }
        logger.info { "$messagePrefix Deleted count = ${e.writeResult.deletedCount}" }
        logger.info { "$messagePrefix Modified count = ${e.writeResult.modifiedCount}" }
        logger.info { "$messagePrefix Error count = ${e.writeErrors.size}" }
        e.writeErrors.forEach { error ->
            logger.error { "Error message = ${error.message}" }
        }
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}
