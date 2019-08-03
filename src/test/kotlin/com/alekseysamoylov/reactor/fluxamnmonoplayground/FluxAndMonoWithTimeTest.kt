package com.alekseysamoylov.reactor.fluxamnmonoplayground

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration


class FluxAndMonoWithTimeTest {

  @Test
  fun infiniteSequence() {
    val infiniteFlux = Flux.interval(Duration.ofMillis(200))
        .log()

    infiniteFlux
        .subscribe { println("Value is: $it") }

    Thread.sleep(3000)
  }

  @Test
  fun infiniteSequenceTest() {
    val finiteFlux = Flux.interval(Duration.ofMillis(200))
        .take(3)
        .log()

    StepVerifier.create(finiteFlux)
        .expectSubscription()
        .expectNext(0, 1,2)
        .verifyComplete()
  }

  @Test
  fun infiniteSequenceTestWithDelay() {
    val finiteFlux = Flux.interval(Duration.ofMillis(200))
        .delayElements(Duration.ofSeconds(1))
        .take(3)
        .log()

    StepVerifier.create(finiteFlux)
        .expectSubscription()
        .expectNext(0, 1,2)
        .verifyComplete()
  }
}
