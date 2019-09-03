package com.alekseysamoylov.reactor.fluxandmonoplayground

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration


class FluxAndMonoErrorTest {


  @Test
  fun fluxErrorHandling() {
    val stringFlux = Flux.just("A","B","C")
        .concatWith(Flux.error(RuntimeException("Exception Occurred")))
        .concatWith(Flux.just("D"))
        .onErrorResume {
          println("Exception is $it")
           Flux.just("default", "default1")
        }

    StepVerifier.create(stringFlux.log())
        .expectSubscription()
        .expectNext("A","B","C")
//        .expectError(RuntimeException::class.java)
//        .verify()
        .expectNext("default", "default1")
        .verifyComplete()
  }

  @Test
  fun fluxErrorHandlingOnErrorReturn() {
    val stringFlux = Flux.just("A","B","C")
        .concatWith(Flux.error(RuntimeException("Exception Occurred")))
        .concatWith(Flux.just("D"))
        .onErrorReturn("default")

    StepVerifier.create(stringFlux.log())
        .expectSubscription()
        .expectNext("A","B","C")
        .expectNext("default")
        .verifyComplete()
  }

  @Test
  fun fluxErrorHandlingOnErrorMap() {
    val stringFlux = Flux.just("A","B","C")
        .concatWith(Flux.error(RuntimeException("Exception Occurred")))
        .concatWith(Flux.just("D"))
        .onErrorMap { IllegalArgumentException(it.message) }

    StepVerifier.create(stringFlux.log())
        .expectSubscription()
        .expectNext("A","B","C")
        .expectError(java.lang.IllegalArgumentException::class.java)
        .verify()
  }

  @Test
  fun fluxErrorHandlingOnErrorMapWithRetry() {
    val stringFlux = Flux.just("A","B","C")
        .concatWith(Flux.error(RuntimeException("Exception Occurred")))
        .concatWith(Flux.just("D"))
        .onErrorMap { IllegalArgumentException(it.message) }
        .retry(2)

    StepVerifier.create(stringFlux.log())
        .expectSubscription()
        .expectNext("A","B","C")
        .expectNext("A","B","C")
        .expectNext("A","B","C")
        .expectError(java.lang.IllegalArgumentException::class.java)
        .verify()
  }
  @Test
  fun fluxErrorHandlingOnErrorMapWithRetryBackoff() {
    val stringFlux = Flux.just("A","B","C")
        .concatWith(Flux.error(RuntimeException("Exception Occurred")))
        .concatWith(Flux.just("D"))
        .onErrorMap { IllegalArgumentException(it.message) }
        .retryBackoff(2, Duration.ofSeconds(5))

    StepVerifier.create(stringFlux.log())
        .expectSubscription()
        .expectNext("A","B","C")
        .expectNext("A","B","C")
        .expectNext("A","B","C")
        .expectError(java.lang.IllegalArgumentException::class.java)
        .verify()
  }
}
