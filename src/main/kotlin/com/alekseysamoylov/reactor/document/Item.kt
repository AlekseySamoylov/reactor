package com.alekseysamoylov.reactor.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Item(@Id var id: String = "", var description: String = "", var price: Double = 0.0)
