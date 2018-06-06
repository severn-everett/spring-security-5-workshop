package com.example.library.server.api

import com.example.library.server.business.BookService
import com.example.library.server.business.UserResource
import com.example.library.server.business.UserService
import com.example.library.server.common.Role
import com.fasterxml.jackson.databind.ObjectMapper
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
@WebMvcTest(UserRestController::class)
@AutoConfigureRestDocs
@DisplayName("Verify user api")
class UserApiIntegrationTests(private val webClient: MockMvc) {

  @MockBean
  private lateinit var userService: UserService

  @MockBean
  private lateinit var bookService: BookService

  @Test
  @DisplayName("to get list of users")
  @WithMockUser(roles = ["ADMIN"])
  fun verifyAndDocumentGetUsers() {

    val userId = UUID.randomUUID()
    given(userService.findAll())
      .willReturn(listOf(
          UserResource(
              userId,
              "test@example.com",
              "first",
              "last",
              listOf(Role.USER)
          )
      ))

    webClient
      .perform(
          get("/users")
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)))
      .andExpect(status().isOk)
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
      .andDo(
          document(
              "get-users",
              preprocessResponse(prettyPrint())))
  }

  @Test
  @DisplayName("to get single user")
  @WithMockUser(roles = ["ADMIN"])
  fun verifyAndDocumentGetUser() {

    val userId = UUID.randomUUID()

    given(userService.findById(userId)).willReturn(
        UserResource(
            userId,
            "test@example.com",
            "first",
            "last",
            listOf(Role.USER)
        )
    )

    webClient
      .perform(
          get("/users/{userId}", userId)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)))
      .andExpect(status().isOk)
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
      .andDo(
          document(
              "get-user",
              preprocessResponse(prettyPrint())))
  }

  @Test
  @DisplayName("to delete a user")
  @WithMockUser(roles = ["ADMIN"])
  fun verifyAndDocumentDeleteUser() {

    val userId = UUID.randomUUID()

    webClient
      .perform(
          delete("/users/{userId}", userId).with(csrf())
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)))
      .andExpect(status().isNoContent)
      .andDo(
          document(
              "delete-user",
              preprocessResponse(prettyPrint())))

    verify(userService).deleteById(eq(userId))
  }

  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("to create a new user")
  @WithMockUser(roles = ["ADMIN"])
  fun verifyAndDocumentCreateUser() {

    val userResource = UserResource(
        null,
        "test@example.com",
        "first",
        "last",
        listOf(Role.USER))

    webClient
      .perform(
          post("/users").with(csrf())
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(ObjectMapper().writeValueAsString(userResource))
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)))
      .andExpect(status().isOk)
      .andDo(
          document(
              "create-user",
              preprocessResponse(prettyPrint())))
  }
}
