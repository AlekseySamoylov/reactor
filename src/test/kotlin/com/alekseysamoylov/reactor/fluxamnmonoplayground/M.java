package com.alekseysamoylov.reactor.fluxamnmonoplayground;


import java.time.Duration;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class M {


  @Test
  public void combine() {
    Flux<String> flux1 = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
    Flux<String>  flux2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));

    Flux<String>  mergedFlux = Flux.zip(flux1, flux2, String::concat);

    StepVerifier.create(mergedFlux.log())
        .expectSubscription()
        .expectNext("A","B","C","D","E","F")
        .verifyComplete();

  }

}
