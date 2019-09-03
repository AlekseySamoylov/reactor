package com.alekseysamoylov.reactor.repository

import com.alekseysamoylov.reactor.document.Item
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux


interface ItemReactiveRepository : ReactiveMongoRepository<Item, String> {
  fun findByDescription(description: String) : Flux<Item>
}
