package com.example.library.server.dataaccess

import com.example.library.server.common.NoArgConstructor
import com.example.library.server.common.Role
import org.apache.commons.lang3.builder.ToStringBuilder
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceConstructor
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "users")
@NoArgConstructor
class User @PersistenceConstructor constructor(
    @Id
    val id: UUID?,

    @Indexed
    val email: String?,

    val password: String?,

    val firstName: String?,

    val lastName: String?,

    val roles: List<Role>?
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