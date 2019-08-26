package com.alekseysamoylov.reactor.repository

import com.alekseysamoylov.reactor.document.Item
import org.apache.logging.log4j.LogManager
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.*


@DataMongoTest
class ItemReactiveRepositoryTest(
    @Autowired private val itemReactiveRepository: ItemReactiveRepository
) {

  private val logger = LogManager.getLogger()
  private val items = listOf(
      Item(UUID.randomUUID().toString(), "Apple Iphone XXL", 5000.0),
      Item(UUID.randomUUID().toString(), "Apple Iphone XXL", 10000.0),
      Item("ABC", "Apple Iphone XXXL", 10000.0)
  )

  @BeforeEach
  fun setUp() {
    itemReactiveRepository.deleteAll()
        .thenMany(Flux.fromIterable(items))
        .flatMap { itemReactiveRepository.save(it) }
        .doOnNext {logger.info("Insert item $it")}
        .blockLast() // only for test cases
  }

  @AfterEach
  fun clean() {
    itemReactiveRepository.deleteAll()
        .block() // only for test cases
  }

  @Test
  fun getAllItems() {
    StepVerifier.create(itemReactiveRepository.findAll())
        .expectSubscription()
        .expectNextCount(3)
        .verifyComplete()
  }

  @Test
  fun getItemById() {
    StepVerifier.create(itemReactiveRepository.findById("ABC"))
        .expectSubscription()
        .expectNextMatches {it.description == "Apple Iphone XXXL"}
        .verifyComplete()
  }
}
