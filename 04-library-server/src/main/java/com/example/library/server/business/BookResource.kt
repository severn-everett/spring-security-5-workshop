package com.example.library.server.business

import com.example.library.server.common.NoArgConstructor
import java.util.*

@NoArgConstructor
class BookResource(
    var id: UUID?,
    var isbn: String?,
    var title: String?,
    var description: String?,
    var authors: List<String>?,
    var borrowed: Boolean,
    var borrowedBy: UserResource?
)
