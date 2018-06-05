package com.example.library.server.business

import java.util.*

class BookResource(
    var id: UUID?,
    var isbn: String?,
    var title: String?,
    var description: String?,
    var authors: List<String>?,
    var borrowed: Boolean?,
    var borrowedBy: UserResource?
)
