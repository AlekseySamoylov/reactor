package com.alekseysamoylov.reactor.repository

import com.alekseysamoylov.reactor.document.Item
import org.apache.logging.log4j.LogManager
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.annotation.DirtiesContext
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.*


@DataMongoTest
@DirtiesContext
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

  @Test
  fun findItemByDescription() {
    StepVerifier.create(itemReactiveRepository.findByDescription("Apple Iphone XXL"))
        .expectSubscription()
        .expectNextMatches {it.price == 5000.0}
        .expectNextCount(1)
        .verifyComplete()
  }

  @Test
  fun saveItem() {
    val item = Item("CDE", "Porsche 911 Turbo", 100000.0)

    val savedItem = itemReactiveRepository.save(item)

    StepVerifier.create(savedItem.log("saveItem: "))
        .expectSubscription()
        .expectNextMatches {it.description == "Porsche 911 Turbo"}
        .verifyComplete()
  }

  @Test
  fun updateItem() {
    val updatedItem = itemReactiveRepository.findByDescription("Apple Iphone XXXL")
        .map { item ->
          item.price = 12000.0
          item
        }
        .flatMap {item -> itemReactiveRepository.save(item) }

    StepVerifier.create(updatedItem.log("updatedItem: "))
        .expectSubscription()
        .expectNextMatches {it.price == 12000.0}
        .verifyComplete()
  }

  @Test
  fun deleteItem() {
    val deletedItem = itemReactiveRepository.findById("ABC")
        .map(Item::id)
        .flatMap {itemId -> itemReactiveRepository.deleteById(itemId) }

    StepVerifier.create(deletedItem.log("deleteItem"))
        .expectSubscription()
        .verifyComplete()

    StepVerifier.create(itemReactiveRepository.findAll())
        .expectNextCount(2)
        .verifyComplete()
  }
}
