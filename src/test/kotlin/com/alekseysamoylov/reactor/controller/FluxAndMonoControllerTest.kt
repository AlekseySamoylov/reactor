package com.alekseysamoylov.reactor.controller

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.test.StepVerifier

@DirtiesContext
@SpringBootTest
@AutoConfigureWebTestClient
class FluxAndMonoControllerTest {

  @Autowired
  lateinit var webTestClient: WebTestClient

  @Tag("slow")
  @Test
  fun fluxApproachOne() {
    val integerFlux = webTestClient.get().uri("/flux")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus().isOk
        .returnResult(Int::class.java)
        .responseBody

    StepVerifier.create(integerFlux)
        .expectSubscription()
        .expectNext(1, 2, 3, 4)
        .verifyComplete()
  }

  @Tag("slow")
  @Test
  fun fluxApproachSecond() {
    webTestClient.get().uri("/flux")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus().isOk
        .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
        .expectBodyList(Int::class.java)
        .hasSize(4)
  }

  @Tag("slow")
  @Test
  fun fluxApproachThird() {
    val expectedList = listOf(1, 2, 3, 4)
    val entityExchangeResult = webTestClient.get().uri("/flux")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus().isOk
        .expectBodyList(Int::class.java)
        .returnResult()

    Assertions.assertEquals(expectedList, entityExchangeResult.responseBody)
  }

  @Tag("slow")
  @Test
  fun fluxApproachFourth() {
    val expectedList = listOf(1, 2, 3, 4)

    val entityExchangeResult = webTestClient.get().uri("/flux")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus().isOk
        .expectBodyList(Int::class.java)
        .consumeWith<WebTestClient.ListBodySpec<Int>> { Assertions.assertEquals(expectedList, it.responseBody) }

    Assertions.assertEquals(expectedList, entityExchangeResult.returnResult().responseBody)
  }

  @Tag("slow")
  @Test
  fun fluxStream() {
    val longStreamFlux = webTestClient.get().uri("/fluxstream")
        .accept(MediaType.APPLICATION_STREAM_JSON)
        .exchange()
        .expectStatus().isOk
        .returnResult(Long::class.java)
        .responseBody

    StepVerifier.create(longStreamFlux)
        .expectNext(0L)
        .expectNext(1L)
        .expectNext(2L)
        .thenCancel()
        .verify()
  }

  @Tag("slow")
  @Test
  fun mono() {
    val expectedValue = 1
    val result = webTestClient.get().uri("/mono")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk
        .expectBody(Int::class.java)
        .returnResult()
        .responseBody

    assertEquals(expectedValue, result)
  }
}
