package com.example.library.server.business

import com.example.library.server.dataaccess.User
import com.example.library.server.dataaccess.UserRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.util.IdGenerator
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
@PreAuthorize("hasRole('ADMIN')")
open class UserService(
    private val userRepository: UserRepository,
    private val idGenerator: IdGenerator
) {

  @PreAuthorize("isAnonymous() or isAuthenticated()")
  open fun findOneByEmail(email: String): Mono<UserResource> =
    userRepository.findOneByEmail(email).map(this::convert)

  open fun create(userResource: Mono<UserResource>): Mono<Void> =
    userRepository.insert(userResource.map(this::convert)).then()

  open fun findById(uuid: UUID): Mono<UserResource> =
    userRepository.findById(uuid).map(this::convert)

  open fun findAll(): Flux<UserResource> =
    userRepository.findAll().map(this::convert)

  open fun deleteById(uuid: UUID): Mono<Void> =
    userRepository.deleteById(uuid)

  private fun convert(u: User): UserResource =
    UserResource(u.id, u.email, u.password, u.firstName, u.lastName, u.roles)

  private fun convert(ur: UserResource): User =
    User(ur.id ?: idGenerator.generateId(), ur.email, ur.password, ur.firstName, ur.lastName, ur.roles)
}
