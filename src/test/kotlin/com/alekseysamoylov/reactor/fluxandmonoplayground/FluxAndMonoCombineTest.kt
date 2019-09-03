package com.alekseysamoylov.reactor.fluxandmonoplayground

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import reactor.test.scheduler.VirtualTimeScheduler
import java.time.Duration
import java.util.function.BiFunction


class FluxAndMonoCombineTest {

  @Test
  fun combineUsingMerge() {
    val flux1 = Flux.just("A","B","C")
    val flux2 = Flux.just("D","E","F")

    val mergedFlux = Flux.merge(flux1, flux2)

    StepVerifier.create(mergedFlux.log())
        .expectSubscription()
        .expectNext("A","B","C","D","E","F")
        .verifyComplete()
  }

  @Test
  fun combineUsingMergeWithDelay() {
    VirtualTimeScheduler.getOrSet()

    val flux1 = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1))
    val flux2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1))

    val mergedFlux = Flux.merge(flux1, flux2)

    StepVerifier.withVirtualTime { mergedFlux.log() }
        .expectSubscription()
        .thenAwait(Duration.ofSeconds(6))
        .expectNextCount(6)
        .verifyComplete()

//    StepVerifier.create(mergedFlux.log())
//        .expectSubscription()
//        .expectNextCount(6)
////        .expectNext("A","B","C","D","E","F")
//        .verifyComplete()
  }

  @Test
  fun combineUsingConcatWithDelay() {
    val flux1 = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1))
    val flux2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1))

    val mergedFlux = Flux.concat(flux1, flux2)

    StepVerifier.create(mergedFlux.log())
        .expectSubscription()
        .expectNext("A","B","C","D","E","F")
        .verifyComplete()
  }

  @Test
  fun combineUsingZiptWithDelay() {
    val flux1 = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1))
    val flux2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1))

    val mergedFlux = Flux.zip(flux1, flux2, BiFunction<String, String, String> { t1, t2 -> t1 + t2 })

    StepVerifier.create(mergedFlux.log())
        .expectSubscription()
        .expectNext("AD","BE","CF")
        .verifyComplete()
  }
}
