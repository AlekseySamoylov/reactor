package com.alekseysamoylov.reactor.controller.v1

import com.alekseysamoylov.reactor.constants.ITEM_ENDPOINT_V1
import com.alekseysamoylov.reactor.document.Item
import com.alekseysamoylov.reactor.repository.ItemReactiveRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux


@RestController
class ItemController(private val itemReactiveRepository: ItemReactiveRepository) {

  @GetMapping(ITEM_ENDPOINT_V1)
  fun getAllItems(): Flux<Item> {
    return itemReactiveRepository.findAll()
  }
}
