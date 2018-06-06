package com.example.oauth2loginclient.api

import java.util.*

class BookResource(
    val id: UUID,
    val isbn: String,
    val title: String,
    val description: String,
    val authors: List<String>,
    val borrowed: Boolean,
    val borrowedBy: UserInfo?
)
