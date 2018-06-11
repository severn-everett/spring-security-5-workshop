package com.example.library.server.api

import com.example.library.server.business.BookResource
import com.example.library.server.business.BookService
import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@ExtendWith(SpringExtension::class)
@WebFluxTest(BookRestController::class)
@AutoConfigureRestDocs
@DisplayName("Verify book api")
class BookApiIntegrationTests(@Autowired private val webClient: WebTestClient) {

  @MockBean
  private lateinit var bookService: BookService

  @Test
  @DisplayName("to get list of books")
  fun verifyAndDocumentGetBooks() {
    val bookId = UUID.randomUUID()
    given(bookService.findAll())
      .willReturn(
          Flux.just(
              BookResource(
                  bookId,
                  "1234566",
                  "title",
                  "description",
                  Collections.singletonList("Author"),
                  false,
                  null)))

    webClient
      .get()
      .uri("/books")
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .json(
          "[{\"id\":\"$bookId\",\"isbn\":\"1234566\",\"title\":\"title\",\"description\":\"description\",\"authors\":[\"Author\"],\"borrowed\":false,\"borrowedBy\":null}]")
      .consumeWith(document("get-books"))
  }

  @Test
  @DisplayName("to get single book")
  fun verifyAndDocumentGetBook() {

    val bookId = UUID.randomUUID()
    given(bookService.findById(bookId))
      .willReturn(
          Mono.just(
              BookResource(
                  bookId,
                  "1234566",
                  "title",
                  "description",
                  Collections.singletonList("Author"),
                  false,
                  null)))

    webClient
      .get().uri("/books/{bookId}", bookId).accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .json(
          "{\"id\":\"$bookId\",\"isbn\":\"1234566\",\"title\":\"title\",\"description\":\"description\",\"authors\":[\"Author\"],\"borrowed\":false,\"borrowedBy\":null}")
      .consumeWith(document("get-book"))
  }

  @Test
  @DisplayName("to delete a book")
  fun verifyAndDocumentDeleteBook() {

    val bookId = UUID.randomUUID()
    given(bookService.deleteById(bookId)).willReturn(Mono.empty())

    webClient
      .delete().uri("/books/{bookId}", bookId).accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .consumeWith(document("delete-book"))

    verify(bookService).deleteById(bookId)
  }

  @Test
  @DisplayName("to borrow a book")
  fun verifyAndDocumentBorrowBook() {

    val bookId = UUID.randomUUID()

    webClient
      .post().uri("/books/{bookId}/borrow", bookId).accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .consumeWith(document("borrow-book"))

    verify(bookService).borrowById(anyOrNull(), anyOrNull())
  }

  @Test
  @DisplayName("to return a borrowed book")
  fun verifyAndDocumentReturnBook() {

    val bookId = UUID.randomUUID()

    webClient
      .post().uri("/books/{bookId}/return", bookId).accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .consumeWith(document("return-book"))

    verify(bookService).returnById(anyOrNull(), anyOrNull())
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("to create a new book")
  fun verifyAndDocumentCreateBook() {

    val bookResource =
      BookResource(
          null,
          "1234566",
          "title",
          "description",
          Collections.singletonList("Author"),
          false,
          null)

    given(bookService.create(any())).willAnswer { i ->
      (i.getArgument(0) as Mono<BookResource>).subscribe()
      Mono.empty<Void>()
    }

    webClient
      .post().uri("/books").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromObject(
          ObjectMapper().writeValueAsString(bookResource)))
      .exchange()
      .expectStatus()
      .isOk
      .expectBody().consumeWith(document("create-book"))
  }

}
