package com.alekseysamoylov.reactor.controller

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.EntityExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import reactor.test.scheduler.VirtualTimeScheduler
import java.time.Duration
import java.util.function.Consumer

@WebFluxTest
class FluxAndMonoControllerTest {

  @Autowired
  lateinit var webTestClient: WebTestClient

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

  @Test
  fun fluxApproachFourth() {
    val expectedList = listOf(1, 2, 3, 4)

    val entityExchangeResult = webTestClient.get().uri("/flux")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus().isOk
        .expectBodyList(Int::class.java)
        .consumeWith<WebTestClient.ListBodySpec<Int>> { Assertions.assertEquals(expectedList, it.responseBody) }

  }
}
