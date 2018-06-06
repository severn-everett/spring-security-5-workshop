package com.example.library.server.business

import com.example.library.server.dataaccess.User
import com.example.library.server.dataaccess.UserRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.util.IdGenerator
import java.util.*

@Service
@PreAuthorize("hasRole('ADMIN')")
class UserService(
    private val userRepository: UserRepository,
    private val idGenerator: IdGenerator
) {

  @PreAuthorize("isAnonymous() or isAuthenticated()")
  fun findOneByEmail(email: String): UserResource? =
    userRepository.findOneByEmail(email).map(this::convert).orElse(null)

  fun create(userResource: UserResource) {
    userRepository.insert(convert(userResource))
  }

  fun findById(uuid: UUID): UserResource? =
    userRepository.findById(uuid).map(this::convert).orElse(null)

  fun findAll(): List<UserResource> = userRepository.findAll().map(this::convert)

  fun deleteById(uuid: UUID) {
    userRepository.deleteById(uuid)
  }

  private fun convert(u: User): UserResource =
    UserResource(u.id, u.email, u.firstName, u.lastName, u.roles)

  private fun convert(ur: UserResource): User =
    User(ur.id ?: idGenerator.generateId(), ur.email, ur.firstName, ur.lastName, ur.roles)
}
