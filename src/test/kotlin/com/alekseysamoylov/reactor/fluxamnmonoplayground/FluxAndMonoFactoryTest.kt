package com.alekseysamoylov.reactor.fluxamnmonoplayground

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier


class FluxAndMonoFactoryTest {

  private val names = listOf("adam", "anna","jack","jenny")

  @Test
  fun fluxUsingIterable() {
    val namesFlux = Flux.fromIterable(names)

    StepVerifier.create(namesFlux)
        .expectNext("adam", "anna","jack","jenny")
        .verifyComplete()
  }

  @Test
  fun fluxUsingArray() {
    val names = arrayOf("adam", "anna","jack","jenny")
    val namesFlux = Flux.fromArray(names)

    StepVerifier.create(namesFlux)
        .expectNext("adam", "anna","jack","jenny")
        .verifyComplete()
  }

  @Test
  fun fluxUsingStream() {
    val namesFlux = Flux.fromStream(names.stream())

    StepVerifier.create(namesFlux)
        .expectNext("adam", "anna","jack","jenny")
        .verifyComplete()
  }

  @Test
  fun monoUsingJustOrEmpty() {
    val mono = Mono.justOrEmpty<String>(null)

    StepVerifier.create(mono.log())
        .verifyComplete()
  }

  @Test
  fun monoUsingSupplier() {
    val stringSupplier = { "adam" }

    val mono = Mono.fromSupplier(stringSupplier)

    StepVerifier.create(mono.log())
        .expectNext("adam")
        .verifyComplete()
  }

  @Test
  fun fluxUsingRange() {
    val integerFlux = Flux.range(1, 5)

    StepVerifier.create(integerFlux.log())
        .expectNext(1,2,3,4,5)
        .verifyComplete()
  }

}
