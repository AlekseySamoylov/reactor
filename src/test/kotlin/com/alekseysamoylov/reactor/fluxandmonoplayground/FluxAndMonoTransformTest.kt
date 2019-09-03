package com.alekseysamoylov.reactor.fluxandmonoplayground

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import reactor.test.StepVerifier
import java.util.*


class FluxAndMonoTransformTest {
  private val names = listOf("adam", "anna", "jack", "jenny")

  @Test
  fun transformUsingMap() {
    val namesFlux = Flux.fromIterable(names)
        .map { it.toUpperCase() }
        .log()

    StepVerifier.create(namesFlux)
        .expectNext("ADAM", "ANNA", "JACK", "JENNY")
        .verifyComplete()
  }

  @Test
  fun transformLengthUsingMap() {
    val namesFlux = Flux.fromIterable(names)
        .map { it.length }
        .log()

    StepVerifier.create(namesFlux)
        .expectNext(4, 4, 4, 5)
        .verifyComplete()
  }

  @Test
  fun transformLengthRepeatUsingMap() {
    val namesFlux = Flux.fromIterable(names)
        .map { it.length }
        .repeat(1)
        .log()

    StepVerifier.create(namesFlux)
        .expectNext(4, 4, 4, 5, 4, 4, 4, 5)
        .verifyComplete()
  }

  @Test
  fun transformUsingFlatMap() {
    val names = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
        .flatMap { Flux.fromIterable(convertToList(it)) }
        .log()

    StepVerifier.create(names)
        .expectNextCount(12)
        .verifyComplete()
  }

  private fun convertToList(s: String): List<String> {
    Thread.sleep(1000)
    return Arrays.asList(s, "newValue")
  }

  @Test
  fun transformUsingFlatMapUsingParallel() {
    val names = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
        .window(2)
        .flatMap { group ->
          group.map { convertToList(it) }
              .subscribeOn(Schedulers.parallel())
        }
        .flatMap { Flux.fromIterable(it) }
        .log()

    StepVerifier.create(names)
        .expectNextCount(12)
        .verifyComplete()
  }

  @Test
  fun transformUsingFlatMapUsingParallelMaintainOrder() {
    val names = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
        .window(2)
//        .concatMap { group ->
//          group.map { convertToList(it) }
//              .subscribeOn(Schedulers.parallel())
//        }
        .flatMapSequential { group ->
          group.map { convertToList(it) }
              .subscribeOn(Schedulers.parallel())
        }
        .flatMap { Flux.fromIterable(it) }
        .log()

    StepVerifier.create(names)
        .expectNextCount(12)
        .verifyComplete()
  }

}
