package com.alekseysamoylov.reactor.fluxandmonoplayground

import org.junit.jupiter.api.Test
import reactor.core.publisher.BaseSubscriber
import reactor.core.publisher.Flux
import reactor.test.StepVerifier


class FluxAndMonoBackpressureTest {

  @Test
  fun backPressureTest() {
    val finiteFlux = Flux.range(1, 10)
        .log()

    StepVerifier.create(finiteFlux)
        .expectSubscription()
        .thenRequest(1)
        .expectNext(1)
        .thenRequest(2)
        .expectNext(2, 3)
        .thenCancel()
        .verify()
  }

  @Test
  fun backPressure() {
    val finiteFlux = Flux.range(1, 10)
        .log()

    finiteFlux.subscribe({
      println("Element is $it")
    }, {
      println("Exception is ${it.message}")
    }, {
      println("Done")
    }, {
      it.request(2)
    })
  }

  @Test
  fun backPressureCancel() {
    val finiteFlux = Flux.range(1, 10)
        .log()

    finiteFlux.subscribe({
      println("Element is $it")
    }, { exception ->
      println("Exception is ${exception.message}")
    }, {
      println("Done")
    }, {
      it.cancel()
    })
  }

  @Test
  fun customizedBackPressure() {
    val finiteFlux = Flux.range(1, 10)
        .log()

    finiteFlux.subscribe(object : BaseSubscriber<Int>() {
      override fun hookOnNext(value: Int) {
        request(1)
        println("Value received is : $value")
        if (value == 4) {
          cancel()
        }
      }
    })
  }
}
