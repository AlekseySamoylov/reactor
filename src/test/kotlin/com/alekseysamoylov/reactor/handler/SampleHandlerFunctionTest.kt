package com.alekseysamoylov.reactor.handler

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.test.StepVerifier

@SpringBootTest
@AutoConfigureWebTestClient
class SampleHandlerFunctionTest {

  @Autowired
  private lateinit var webTestClient: WebTestClient

  @Test
  fun fluxApproachOne() {
    val integerFlux = webTestClient.get().uri("/functional/flux")
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
  fun mono() {
    val expectedValue = 1
    val result = webTestClient.get().uri("/functional/mono")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk
        .expectBody(Int::class.java)
        .returnResult()
        .responseBody

    Assertions.assertEquals(expectedValue, result)
  }

}
