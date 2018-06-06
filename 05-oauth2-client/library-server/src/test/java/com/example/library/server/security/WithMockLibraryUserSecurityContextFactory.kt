package com.example.library.server.security

import com.example.library.server.business.UserResource
import com.example.library.server.common.Role
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContextFactory
import java.util.*
import kotlin.collections.ArrayList

class WithMockLibraryUserSecurityContextFactory : WithSecurityContextFactory<WithMockLibraryUser> {

  override fun createSecurityContext(withUser: WithMockLibraryUser): SecurityContext {

    val roles = ArrayList<Role>()
    roles.add(Role.USER)
    withUser.roles.mapTo(roles, { Role.valueOf(it) })
    val principal = LibraryUser(
        UserResource(
            UUID.randomUUID(),
            "test@example.com",
            "Hans",
            "Mustermann",
            roles
        )
    )

    setIdentifier(withUser, principal)
    val authentication = UsernamePasswordAuthenticationToken(
        principal, principal.password, principal.authorities
    )
    return SecurityContextHolder.createEmptyContext().apply { this.authentication = authentication }
  }

  private fun setIdentifier(withUser: WithMockLibraryUser, principal: LibraryUser) {
    principal.userResource.id = if (withUser.identifier.isBlank())
      UUID.randomUUID()
    else
      UUID.fromString((withUser.identifier))
  }

}
