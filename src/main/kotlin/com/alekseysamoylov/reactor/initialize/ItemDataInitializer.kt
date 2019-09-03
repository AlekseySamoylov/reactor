package com.alekseysamoylov.reactor.initialize

import com.alekseysamoylov.reactor.document.Item
import com.alekseysamoylov.reactor.repository.ItemReactiveRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.util.*

@Component
@Profile("dev")
class ItemDataInitializer(
    private val itemReactiveRepository: ItemReactiveRepository
) : CommandLineRunner {
  override fun run(vararg args: String?) {
    initialDataSetup()
  }

  private fun data(): List<Item> {
    return listOf(
        Item(UUID.randomUUID().toString(), "Apple Iphone XXL", 5000.0),
        Item(UUID.randomUUID().toString(), "Apple Iphone XXL", 10000.0),
        Item("ABC", "Apple Iphone XXXL", 10000.0)    )
  }

  private fun initialDataSetup() {
    itemReactiveRepository.deleteAll()
        .thenMany(Flux.fromIterable(data()))
        .flatMap { item -> itemReactiveRepository.save(item) }
        .subscribe()
  }
}
