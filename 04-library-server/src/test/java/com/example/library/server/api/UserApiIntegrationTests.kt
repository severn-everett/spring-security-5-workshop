package com.example.library.server.api

import com.example.library.server.business.BookService
import com.example.library.server.business.UserResource
import com.example.library.server.business.UserService
import com.example.library.server.common.Role
import com.example.library.server.config.UserRouter
import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@ExtendWith(SpringExtension::class)
@WebFluxTest
@Import(UserHandler::class, UserRouter::class)
@AutoConfigureRestDocs
@DisplayName("Verify user api")
class UserApiIntegrationTests(@Autowired private val webClient: WebTestClient) {

  @MockBean
  private lateinit var userService: UserService

  @MockBean
  private lateinit var bookService: BookService

  @Test
  @DisplayName("to get list of users")
  fun verifyAndDocumentGetUsers() {

    val userId = UUID.randomUUID()
    given(userService.findAll())
      .willReturn(
          Flux.just(
              UserResource(
                  userId,
                  "test@example.com",
                  "test",
                  "first",
                  "last",
                  Collections.singletonList(Role.USER))))

    webClient
      .get()
      .uri("/users")
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .json(
          "[{\"id\":\"$userId\",\"email\":\"test@example.com\",\"firstName\":\"first\",\"lastName\":\"last\"}]")
      .consumeWith(document("get-users"))
  }

  @Test
  @DisplayName("to get single user")
  fun verifyAndDocumentGetUser() {

    val userId = UUID.randomUUID()

    given(userService.findById(userId))
      .willReturn(
          Mono.just(
              UserResource(
                  userId,
                  "test@example.com",
                  "test",
                  "first",
                  "last",
                  Collections.singletonList(Role.USER))))

    webClient
      .get().uri("/users/{userId}", userId).accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .json("{\"id\":\"$userId\",\"email\":\"test@example.com\",\"firstName\":\"first\",\"lastName\":\"last\"}")
      .consumeWith(document("get-user"))
  }

  @Test
  @DisplayName("to delete a user")
  fun verifyAndDocumentDeleteUser() {

    val userId = UUID.randomUUID()
    given(userService.deleteById(userId)).willReturn(Mono.empty())

    webClient
      .delete().uri("/users/{userId}", userId).accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .consumeWith(document("delete-user"))

    verify(userService).deleteById(eq(userId))
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("to create a new user")
  fun verifyAndDocumentCreateUser() {
    val userResource =
      UserResource(
          null,
          "test@example.com",
          "test",
          "first",
          "last",
          Collections.singletonList(Role.USER))

    given(userService.create(any())).willAnswer { i ->
      (i.getArgument(0) as Mono<UserResource>).subscribe()
      Mono.empty<Void>()
    }

    webClient
      .post().uri("/users").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromObject(
          ObjectMapper().writeValueAsString(userResource)))
      .exchange()
      .expectStatus()
      .isOk
      .expectBody().consumeWith(document("create-user"))
  }
}
