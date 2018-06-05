package com.example.library.server.api

import com.example.library.server.business.UserResource
import com.example.library.server.business.UserService
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse


import org.springframework.web.reactive.function.server.ServerResponse.notFound
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import java.util.*

/**
 * Reactive handler for users.
 */
@Component
class UserHandler(private val userService: UserService) {

  @SuppressWarnings("unused")
  fun getAllUsers(request: ServerRequest): Mono<ServerResponse> =
    ok().contentType(MediaType.APPLICATION_JSON_UTF8)
      .body(userService.findAll(), UserResource::class.java)

  fun getUser(request: ServerRequest): Mono<ServerResponse> =
    userService.findById(UUID.fromString(request.pathVariable("userId")))
      .flatMap { ur -> ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject(ur)) }
      .switchIfEmpty(notFound().build())

  fun deleteUser(request: ServerRequest): Mono<ServerResponse> =
    ok().build(userService.deleteById(UUID.fromString(request.pathVariable("userId"))))

  fun createUser(request: ServerRequest): Mono<ServerResponse> =
    ok().build(userService.create(request.bodyToMono(UserResource::class.java)))

}
