package com.example.library.server.business

import com.example.library.server.LibraryServerApplication
import com.example.library.server.common.Role
import com.example.library.server.dataaccess.User
import com.example.library.server.dataaccess.UserRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.util.IdGenerator
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

@DisplayName("Verify that user service")
@SpringJUnitConfig(LibraryServerApplication::class)
class UserServiceTest(private val userService: UserService) {

  @MockBean
  private lateinit var userRepository: UserRepository

  @SuppressWarnings("unused")
  @MockBean
  private lateinit var idGenerator: IdGenerator

  @DisplayName("grants access to find one user by email for anonymous user")
  @Test
  fun verifyFindOneByEmailAccessIsGrantedForUnauthenticated() {
    whenever(userRepository.findOneByEmail(any())).thenReturn(Mono.just(generateUser()))
    StepVerifier.create(userService.findOneByEmail("test@example.com")).expectNextCount(1).verifyComplete()
  }

  @DisplayName("grants access to find one user by email for roles 'USER', 'CURATOR' and 'ADMIN'")
  @Test
  @WithMockUser(roles = ["USER", "CURATOR", "ADMIN"])
  fun verifyFindOneByEmailAccessIsGrantedForAllRoles() {
    whenever(userRepository.findOneByEmail(any())).thenReturn(Mono.just(generateUser()))
    StepVerifier.create(userService.findOneByEmail("test@example.com")).expectNextCount(1).verifyComplete()
  }

  @DisplayName("grants access to create a user for role 'ADMIN'")
  @Test
  @WithMockUser(roles = ["ADMIN"])
  fun verifyCreateAccessIsGrantedForAdmin() {
    whenever(userRepository.insert(any<Mono<User>>())).thenReturn(Flux.just(generateUser()))
    StepVerifier.create(userService.create(Mono.just(UserResource(UUID.randomUUID(),
        "test@example.com", "secret", "Max", "Maier",
        listOf(Role.USER))))).verifyComplete()
  }

  @DisplayName("denies access to create a user for roles 'USER' and 'CURATOR'")
  @Test
  @WithMockUser(roles = ["USER", "CURATOR"])
  fun verifyCreateAccessIsDeniedForUserAndCurator() {
    StepVerifier.create(userService.create(Mono.just(UserResource(UUID.randomUUID(),
        "test@example.com", "secret", "Max", "Maier",
        Collections.singletonList(Role.USER))))).verifyError(AccessDeniedException::class.java)
  }

  @DisplayName("denies access to create a user for anonymous user")
  @Test
  fun verifyCreateAccessIsDeniedForUnauthenticated() {
    StepVerifier.create(userService.create(Mono.just(UserResource(UUID.randomUUID(),
        "test@example.com", "secret", "Max", "Maier",
        Collections.singletonList(Role.USER))))).verifyError(AccessDeniedException::class.java)
  }

  @DisplayName("grants access to find a user by id for role 'ADMIN'")
  @Test
  @WithMockUser(roles = ["ADMIN"])
  fun verifyFindByIdAccessIsGrantedForAdmin() {
    whenever(userRepository.findById(any<UUID>())).thenReturn(Mono.just(generateUser()))
    StepVerifier.create(userService.findById(UUID.randomUUID())).expectNextCount(1).verifyComplete()
  }

  @DisplayName("denies access to find a user by id for roles 'USER' and 'CURATOR'")
  @Test
  @WithMockUser(roles = ["USER", "CURATOR"])
  fun verifyFindByIdAccessIsDeniedForUserAndCurator() {
    StepVerifier.create(userService.findById(UUID.randomUUID())).verifyError(AccessDeniedException::class.java)
  }

  @DisplayName("denies access to find a user by id for anonymous user")
  @Test
  fun verifyFindByIdAccessIsDeniedForUnauthenticated() {
    StepVerifier.create(userService.findById(UUID.randomUUID())).verifyError(AccessDeniedException::class.java)
  }

  @DisplayName("grants access to find all users for role 'ADMIN'")
  @Test
  @WithMockUser(roles = ["ADMIN"])
  fun verifyFindAllAccessIsGrantedForAdmin() {
    whenever(userRepository.findAll()).thenReturn(Flux.just(generateUser()))
    StepVerifier.create(userService.findAll()).expectNextCount(1).verifyComplete()
  }

  @DisplayName("denies access to find all users for roles 'USER' and 'CURATOR'")
  @Test
  @WithMockUser(roles = ["USER", "CURATOR"])
  fun verifyFindAllAccessIsDeniedForUserAndCurator() {
    StepVerifier.create(userService.findAll()).verifyError(AccessDeniedException::class.java)
  }

  @DisplayName("denies access to find all users for anonymous user")
  @Test
  fun verifyFindAllAccessIsDeniedForUnauthenticated() {
    StepVerifier.create(userService.findAll()).verifyError(AccessDeniedException::class.java)
  }

  @DisplayName("grants access to delete a user for role 'ADMIN'")
  @Test
  @WithMockUser(roles = ["ADMIN"])
  fun verifyDeleteByIdAccessIsGrantedForAdmin() {
    whenever(userRepository.deleteById(any<UUID>())).thenReturn(Mono.empty())
    StepVerifier.create(userService.deleteById(UUID.randomUUID())).verifyComplete()
  }

  @DisplayName("denies access to delete a user for roles 'USER' and 'CURATOR'")
  @Test
  @WithMockUser(roles = ["USER", "CURATOR"])
  fun verifyDeleteByIdAccessIsDeniedForUserAndCurator() {
    StepVerifier.create(userService.deleteById(UUID.randomUUID())).verifyError(AccessDeniedException::class.java)
  }

  @DisplayName("denies access to delete a user for anonymous user")
  @Test
  fun verifyDeleteByIdAccessIsDeniedForUnauthenticated() {
    StepVerifier.create(userService.deleteById(UUID.randomUUID())).verifyError(AccessDeniedException::class.java)
  }

  private fun generateUser() =
      User(UUID.randomUUID(), "test@example.com", "secret", "Max", "Maier", listOf(Role.USER))

}