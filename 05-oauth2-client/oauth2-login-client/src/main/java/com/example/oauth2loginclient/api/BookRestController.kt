package com.example.oauth2loginclient.api

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
class BookRestController(private val restTemplate: RestTemplate) {

  @GetMapping("/books")
  fun books(): List<BookResource>? =
    restTemplate.exchange(
        "http://localhost:8080/books",
        HttpMethod.GET,
        null,
        object: ParameterizedTypeReference<List<BookResource>>() {}
    ).body

  @GetMapping("/users")
  fun users(): List<UserResource>? =
    restTemplate.exchange(
        "http://localhost:8080/books",
        HttpMethod.GET,
        null,
        object: ParameterizedTypeReference<List<UserResource>>() {}
    ).body

}
