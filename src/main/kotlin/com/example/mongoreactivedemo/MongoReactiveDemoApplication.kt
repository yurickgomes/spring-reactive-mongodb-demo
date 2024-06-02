package com.example.mongoreactivedemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MongoReactiveDemoApplication

fun main(args: Array<String>) {
	runApplication<MongoReactiveDemoApplication>(*args)
}
