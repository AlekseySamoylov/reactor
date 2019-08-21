package com.alekseysamoylov.reactor.repository

import com.alekseysamoylov.reactor.document.Item
import org.springframework.data.mongodb.repository.ReactiveMongoRepository


interface ItemReactiveRepository : ReactiveMongoRepository<Item, String>
