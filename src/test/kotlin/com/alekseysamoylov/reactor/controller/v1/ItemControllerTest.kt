package com.alekseysamoylov.reactor.controller.v1

import com.alekseysamoylov.reactor.constants.ITEM_ENDPOINT_V1
import com.alekseysamoylov.reactor.document.Item
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.reactive.server.WebTestClient

@DirtiesContext
@SpringBootTest
@Profile("test")
@AutoConfigureWebTestClient
internal class ItemControllerTest {

  @Autowired
  lateinit var webTestClient: WebTestClient
//  @Autowired
//  lateinit var itemReactiveRepository: ItemReactiveRepository

//  @BeforeEach
//  fun setup() {
//    itemReactiveRepository.deleteAll()
//        .thenMany(Flux.fromIterable(data()))
//        .flatMap { item -> itemReactiveRepository.save(item) }
//        .blockLast()
//  }
//
//  private fun data(): List<Item> {
//    return listOf(
//        Item(UUID.randomUUID().toString(), "Apple Iphone XXL", 5000.0),
//        Item(UUID.randomUUID().toString(), "Apple Iphone XXL", 10000.0),
//        Item("ABC", "Apple Iphone XXXL", 10000.0)    )
//  }

  @Test
  fun getAllItems() {
    webTestClient.get().uri(ITEM_ENDPOINT_V1).exchange()
        .expectStatus().isOk
        .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
        .expectBodyList(Item::class.java)
        .hasSize(3)
  }
}
