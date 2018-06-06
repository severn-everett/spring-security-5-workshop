package com.example.library.server.business

import com.example.library.server.common.NoArgConstructor
import com.example.library.server.common.Role
import com.fasterxml.jackson.annotation.JsonIgnore
import org.apache.commons.lang3.builder.ToStringBuilder
import java.util.*

@NoArgConstructor
class UserResource(
    var id: UUID?,
    var email: String?,
    var firstName: String?,
    var lastName: String?,
    @JsonIgnore
    var roles: List<Role>?
) {

  override fun toString(): String =
    ToStringBuilder(this)
      .append("id", id)
      .append("email", email)
      .append("firstName", firstName)
      .append("lastName", lastName)
      .append("roles", roles)
      .toString()
}
