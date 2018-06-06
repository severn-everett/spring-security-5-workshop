package com.example.library.server.dataaccess

import com.example.library.server.common.NoArgConstructor
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceConstructor
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.index.TextIndexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "books")
@NoArgConstructor
class Book @PersistenceConstructor constructor(
    @Id
    val id: UUID?,

    @Indexed(unique = true)
    val isbn: String?,

    @Indexed
    val title: String?,

    @TextIndexed
    val description: String?,

    val authors: List<String>?,

    var borrowed: Boolean,

    var borrowedBy: User?
) {

  fun doBorrow(user: User) {
    if (!borrowed) {
      borrowed = true
      borrowedBy = user
    }
  }

  fun doReturn(user: User) {
    if (borrowed) {
      borrowed = false
      borrowedBy = null
    }
  }
}
