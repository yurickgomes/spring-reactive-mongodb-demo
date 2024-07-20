# spring-reactive-mongodb-demo

Demo application that shows how to get started with spring boot 3 and mongodb reactive.

## Stack

* Spring boot 3
* Kotlin 1.9
* Kotlin Coroutines 1.8
* Project Reactor
* Spring Data MongoDB Reactive

## Reactive styles covered by this demo

* Project reactor with `ReactiveMongoRepository`
    * Examples using `Mono` and `Flux` operators
* Kotlin coroutines with `CoroutineCrudRepository`
    * Examples using `suspendable` functions and `Flow`

## Bulk operations

This repo also contains examples of `ReactiveBulkOperations` usage, to perform bulk operations on a MongoDB collection.

