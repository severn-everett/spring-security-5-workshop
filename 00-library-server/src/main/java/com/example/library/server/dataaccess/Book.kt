package com.example.library.server.dataaccess

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.index.TextIndexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "books")
class Book(
    @Id
    var id: UUID? = null,

    @Indexed(unique = true)
    var isbn: String? = null,

    @Indexed
    var title: String? = null,

    @TextIndexed
    var description: String? = null,

    var authors: List<String>? = null,

    var borrowed: Boolean? = null,

    var borrowedBy: User? = null
) {

  fun doBorrow(user: User) {
    if (!borrowed!!) {
      borrowed = true
      borrowedBy = user
    }
  }

  fun doReturn(user: User) {
    if (borrowed!!) {
      borrowed = false
      borrowedBy = null
    }
  }
}
