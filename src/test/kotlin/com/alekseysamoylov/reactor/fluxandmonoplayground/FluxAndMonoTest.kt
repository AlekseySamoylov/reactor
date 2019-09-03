package com.alekseysamoylov.reactor.fluxandmonoplayground

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier


open class FluxAndMonoTest {

  @Test
  open fun fluxTest() {
    val stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
        .concatWith(Flux.error(RuntimeException("Exception occurred")))
        .concatWith(Flux.just("Apache Camel"))
        .log()

    stringFlux.subscribe({ println(it) }, { System.err.println(it) }, { println("Completed")})

    Assertions.assertTrue(true)
  }

  @Test
  fun fluxTestElementsWithoutError() {
    val stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
        .log()

    StepVerifier.create(stringFlux)
        .expectNext("Spring")
        .expectNext("Spring Boot")
        .expectNext("Reactive Spring")
        .verifyComplete()
  }

  @Test
  fun fluxTestElementsWithtError() {
    val stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
        .concatWith(Flux.error(RuntimeException("Exception occurred")))
        .log()

    StepVerifier.create(stringFlux)
        .expectNext("Spring")
        .expectNext("Spring Boot")
        .expectNext("Reactive Spring")
        .expectErrorMessage("Exception occurred")
        .verify()
  }

  @Test
  fun fluxTestElementsWithtErrorInLine() {
    val stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
        .concatWith(Flux.error(RuntimeException("Exception occurred")))
        .log()

    StepVerifier.create(stringFlux)
        .expectNext("Spring","Spring Boot","Reactive Spring")
        .expectErrorMessage("Exception occurred")
        .verify()
  }

  @Test
  fun fluxTestCountElementsWithtError() {
    val stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
        .concatWith(Flux.error(RuntimeException("Exception occurred")))
        .log()

    StepVerifier.create(stringFlux)
        .expectNextCount(3)
        .expectError(java.lang.RuntimeException::class.java)
        .verify()
  }

  @Test
  fun monoTest() {
    val stringMono = Mono.just("Spring").log()

    StepVerifier.create(stringMono)
        .expectNext("Spring")
        .verifyComplete()
  }


}
