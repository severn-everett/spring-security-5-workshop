package com.example.library.server.business

import com.example.library.server.LibraryServerApplication
import com.example.library.server.common.Role
import com.example.library.server.dataaccess.Book
import com.example.library.server.dataaccess.BookRepository
import com.example.library.server.dataaccess.User
import com.example.library.server.dataaccess.UserRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.util.IdGenerator
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

@DisplayName("Verify that book service")
@SpringJUnitConfig(LibraryServerApplication::class)
class BookServiceTest(private val bookService: BookService) {

  @MockBean
  private lateinit var bookRepository: BookRepository

  @MockBean
  private lateinit var userRepository: UserRepository

  @MockBean
  private lateinit var idGenerator: IdGenerator

  @DisplayName("grants access to create a book for the role 'CURATOR'")
  @Test
  @WithMockUser(roles = ["CURATOR"])
  fun verifyCreateAccessIsGrantedForCurator() {
    whenever(bookRepository.insert(any<Mono<Book>>())).thenReturn(Flux.just(emptyBook()))
    StepVerifier.create(bookService.create(Mono.just(BookResource(UUID.randomUUID(),
        "123456789", "title", "description", listOf("author"),
        false, null)
    ))).verifyComplete()
  }

  @DisplayName("denies access to create a book for roles 'USER' and 'ADMIN'")
  @Test
  @WithMockUser(roles = ["USER", "ADMIN"])
  fun verifyCreateAccessIsDeniedForUserAndAdmin() {
    StepVerifier.create(bookService.create(Mono.just(BookResource(UUID.randomUUID(),
        "123456789", "title", "description", listOf("author"),
        false, null)
    ))).verifyError(AccessDeniedException::class.java)
  }

  @DisplayName("denies access to create a book for anonymous user")
  @Test
  fun verifyCreateAccessIsDeniedForUnauthenticated() {
    StepVerifier.create(bookService.create(Mono.just(BookResource(UUID.randomUUID(),
        "123456789", "title", "description", listOf("author"),
        false, null)
    ))).verifyError(AccessDeniedException::class.java)
  }

  @DisplayName("grants access to find a book by id for role 'USER'")
  @Test
  @WithMockUser
  fun verifyFindByIdAccessIsGrantedForRoleUser() {
    whenever(bookRepository.findById(any<UUID>())).thenReturn(Mono.just(emptyBook()))
    StepVerifier.create(bookService.findById(UUID.randomUUID())).expectNextCount(1).verifyComplete()
  }

  @DisplayName("grants access to find a book by id for role 'CURATOR'")
  @Test
  @WithMockUser(roles = ["CURATOR"])
  fun verifyFindByIdAccessIsGrantedForRoleCurator() {
    whenever(bookRepository.findById(any<UUID>())).thenReturn(Mono.just(emptyBook()))
    StepVerifier.create(bookService.findById(UUID.randomUUID())).expectNextCount(1).verifyComplete()
  }

  @DisplayName("grants access to find a book by id for role 'ADMIN'")
  @Test
  @WithMockUser(roles = ["ADMIN"])
  fun verifyFindByIdAccessIsGrantedForRoleAdmin() {
    whenever(bookRepository.findById(any<UUID>())).thenReturn(Mono.just(emptyBook()))
    StepVerifier.create(bookService.findById(UUID.randomUUID())).expectNextCount(1).verifyComplete()
  }

  @DisplayName("denies access to find a book by id for anonymous user")
  @Test
  fun verifyFindByIdAccessIsDeniedForUnauthenticated() {
    StepVerifier.create(bookService.findById(UUID.randomUUID())).verifyError(AccessDeniedException::class.java)
  }

  @DisplayName("grants access to borrow a book by id for role 'USER'")
  @Test
  @WithMockUser
  fun verifyBorrowByIdAccessIsGrantedForUser() {
    val book = Book(UUID.randomUUID(), "123456", "title", "description", Arrays.asList("author1", "author2"), false, null)
    whenever(bookRepository.findById(any<UUID>())).thenReturn(Mono.just(book))
    whenever(bookRepository.save(any<Book>())).thenReturn(Mono.just(book))
    whenever(userRepository.findById(any<UUID>())).thenReturn(
        Mono.just(User(UUID.randomUUID(), "test@example.com", "secret", "Max",
            "Maier", listOf(Role.USER)))
    )
    StepVerifier.create(bookService.returnById(UUID.randomUUID(), UUID.randomUUID())).verifyComplete()
  }

  @DisplayName("denies access to return a book by id for roles 'CURATOR' or 'ADMIN'")
  @Test
  @WithMockUser(roles = ["CURATOR", "ADMIN"])
  fun verifyReturnByIdAccessIsDeniedForCuratorOrAdmin() {
    StepVerifier.create(bookService.returnById(UUID.randomUUID(), UUID.randomUUID())).verifyError(AccessDeniedException::class.java)
  }

  @DisplayName("denies access to return a book by id for anonymous user")
  @Test
  fun verifyReturnByIdAccessIsDeniedForUnauthenticated() {
    StepVerifier.create(bookService.returnById(UUID.randomUUID(), UUID.randomUUID())).verifyError(AccessDeniedException::class.java)
  }

  @DisplayName("grants access to find all books for role 'USER'")
  @Test
  @WithMockUser
  fun verifyFindAllAccessIsGrantedForUser() {
    whenever(bookRepository.findAll()).thenReturn(Flux.just(emptyBook()))
    StepVerifier.create(bookService.findAll()).expectNextCount(1).verifyComplete()
  }

  @DisplayName("grants access to find all books for role 'CURATOR'")
  @Test
  @WithMockUser(roles = ["CURATOR"])
  fun verifyFindAllAccessIsGrantedForCurator() {
    whenever(bookRepository.findAll()).thenReturn(Flux.just(emptyBook()))
    StepVerifier.create(bookService.findAll()).expectNextCount(1).verifyComplete()
  }

  @DisplayName("grants access to find all books for role 'ADMIN'")
  @Test
  @WithMockUser(roles = ["ADMIN"])
  fun verifyFindAllAccessIsGrantedForAdmin() {
    whenever(bookRepository.findAll()).thenReturn(Flux.just(emptyBook()))
    StepVerifier.create(bookService.findAll()).expectNextCount(1).verifyComplete()
  }

  @DisplayName("denies access to find all books for anonymous user")
  @Test
  @WithAnonymousUser
  fun verifyFindAllAccessIsDeniedForUnauthenticated() {
    StepVerifier.create(bookService.findAll()).verifyError(AccessDeniedException::class.java)
  }

  @DisplayName("grants access to delete a book for role 'CURATOR'")
  @Test
  @WithMockUser(roles = ["CURATOR"])
  fun verifyDeleteByIdAccessIsGrantedForCurator() {
    whenever(bookRepository.deleteById(any<UUID>())).thenReturn(Mono.empty())
    StepVerifier.create(bookService.deleteById(UUID.randomUUID())).verifyComplete()
  }

  @DisplayName("denies access to delete a book for role 'USER' and 'ADMIN'")
  @Test
  @WithMockUser(roles = ["USER", "ADMIN"])
  fun verifyDeleteByIdAccessIsDeniedForUserAndAdmin() {
    StepVerifier.create(bookService.deleteById(UUID.randomUUID())).verifyError(AccessDeniedException::class.java)
  }

  @DisplayName("denies access to delete a book for anonymous user")
  @Test
  fun verifyDeleteByIdAccessIsDeniedForUnauthenticated() {
    StepVerifier.create(bookService.deleteById(UUID.randomUUID())).verifyError(AccessDeniedException::class.java)
  }

  private fun emptyBook() = Book(null, null, null, null, null, false, null)

}
