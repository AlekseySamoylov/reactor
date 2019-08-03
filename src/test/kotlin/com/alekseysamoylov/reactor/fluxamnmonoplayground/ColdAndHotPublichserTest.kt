package com.alekseysamoylov.reactor.fluxamnmonoplayground

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import java.time.Duration


class ColdAndHotPublichserTest {

  @Test
  fun coldPublisherTest() {
    val stringFlux = Flux.just("A","B","C","D","E","F")
        .delayElements(Duration.ofSeconds(1))

    stringFlux.subscribe { println("Subscriber 1 : $it") }
    Thread.sleep(2000)
    stringFlux.subscribe { println("Subscriber 2 : $it") }
    Thread.sleep(4000)
  }

  @Test
  fun hotPublisherTest() {
    val stringFlux = Flux.just("A","B","C","D","E","F")
        .delayElements(Duration.ofSeconds(1))

    val connectableFlux = stringFlux.publish()
    connectableFlux.connect()

    connectableFlux.subscribe { println("Subscriber 1 : $it") }
    Thread.sleep(2000)
    connectableFlux.subscribe { println("Subscriber 2 : $it") }
    Thread.sleep(5000)
  }
}
