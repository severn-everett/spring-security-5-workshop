package com.example.library.server.api

import com.example.library.server.business.BookResource
import com.example.library.server.business.BookService
import com.example.library.server.security.WithMockLibraryUser
import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@ExtendWith(SpringExtension::class)
@WebMvcTest(BookRestController::class)
@AutoConfigureRestDocs
@DisplayName("Verify book api")
class BookApiIntegrationTests(private val webClient: MockMvc) {

  @MockBean
  private lateinit var bookService: BookService

  @Test
  @DisplayName("to get list of books")
  @WithMockUser
  fun verifyAndDocumentGetBooks() {

    val bookId = UUID.randomUUID()
    given(bookService.findAll()).willReturn(listOf(
        BookResource(
            bookId,
            "1234566",
            "title",
            "description",
            listOf("Author"),
            false,
            null
        )
    ))

    webClient
      .perform(
          get("/books")
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)))
      .andExpect(status().isOk)
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
      .andDo(
          document(
              "get-books",
              preprocessResponse(prettyPrint())))
  }

  @Test
  @DisplayName("to get single book")
  @WithMockUser
  fun verifyAndDocumentGetBook() {

    val bookId = UUID.randomUUID()
    given(bookService.findAll()).willReturn(listOf(
        BookResource(
            bookId,
            "1234566",
            "title",
            "description",
            listOf("Author"),
            false,
            null
        )
    ))

    webClient
      .perform(
          get("/books/{bookId}", bookId)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)))
      .andExpect(status().isOk)
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
      .andDo(
          document(
              "get-book",
              preprocessResponse(prettyPrint())))
  }

  @Test
  @DisplayName("to delete a book")
  @WithMockUser
  fun verifyAndDocumentDeleteBook() {

    val bookId = UUID.randomUUID()

    webClient
      .perform(
          delete("/books/{bookId}", bookId).with(csrf())
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)))
      .andExpect(status().isOk)
      .andDo(
          document(
              "delete-book",
              preprocessResponse(prettyPrint())))

    verify(bookService).deleteById(eq(bookId))
  }

  @Test
  @DisplayName("to borrow a book")
  @WithMockLibraryUser
  fun verifyAndDocumentBorrowBook() {

    val bookId = UUID.randomUUID()

    webClient
      .perform(
          post("/books/{bookId}/borrow", bookId).with(csrf())
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)))
      .andExpect(status().isOk)
      .andDo(
          document(
              "borrow-book",
              preprocessResponse(prettyPrint())))

    verify(bookService).borrowById(any(), any())
  }

  @Test
  @DisplayName("to return a borrowed book")
  @WithMockLibraryUser
  fun verifyAndDocumentReturnBook() {

    val bookId = UUID.randomUUID()

    webClient
      .perform(
          post("/books/{bookId}/return", bookId).with(csrf())
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)))
      .andExpect(status().isOk)
      .andDo(
          document(
              "return-book",
              preprocessResponse(prettyPrint())))

    verify(bookService).returnById(any(), any())
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("to create a new book")
  @WithMockUser
  fun verifyAndDocumentCreateBook() {

    val bookResource = BookResource(
        null,
        "1234566",
        "title",
        "description",
        listOf("Author"),
        false,
        null
    )

    webClient
      .perform(
          post("/books").with(csrf())
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(ObjectMapper().writeValueAsString(bookResource))
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)))
      .andExpect(status().isOk)
      .andDo(
          document(
              "create-book",
              preprocessResponse(prettyPrint())))
  }

}
