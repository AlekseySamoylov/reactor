package com.alekseysamoylov.reactor.router

import com.alekseysamoylov.reactor.handler.SampleHandlerFunction
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RequestPredicates.accept
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse


@Configuration
class RouterFunctionConfig {

  @Bean
  fun route(handlerFunction: SampleHandlerFunction) : RouterFunction<ServerResponse> {
    return RouterFunctions
        .route(
            GET("/functional/flux").and(accept(MediaType.APPLICATION_JSON)),
            HandlerFunction { handlerFunction.flux(it) }
        )
        .andRoute(
            GET("/functional/mono").and(accept(MediaType.APPLICATION_JSON)),
            HandlerFunction { handlerFunction.mono(it) }
        )
  }
}
