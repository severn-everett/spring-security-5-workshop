package com.example.library.server.dataaccess

import com.example.library.server.common.Role
import org.apache.commons.lang3.builder.ToStringBuilder
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "users")
class User(
    @Id
    var id: UUID? = null,

    @Indexed
    var email: String? = null,

    var password: String? = null,

    var firstName: String? = null,

    var lastName: String? = null,

    var roles: List<Role>? = null
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