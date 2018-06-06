package com.example.authserver

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.springframework.data.jpa.domain.AbstractPersistable
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.FetchType

/**
 * User entity acting as [UserDetails]
 */
@Entity
class User(
    @JsonProperty("id")
    val identifier: UUID,
    val firstname: String,
    val lastname: String,
    val email: String,
    private val password: String,
    @ElementCollection(fetch = FetchType.EAGER)
    val roles: List<String>
) : AbstractPersistable<Long>(), UserDetails {

  @JsonIgnore
  override fun getId(): Long = super.getId()

  override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
    AuthorityUtils.createAuthorityList("USER")

  @JsonIgnore
  override fun getPassword(): String = password

  override fun getUsername(): String = email

  override fun isAccountNonExpired(): Boolean = true

  override fun isAccountNonLocked(): Boolean = true

  override fun isCredentialsNonExpired(): Boolean = true

  override fun isEnabled(): Boolean = true

  override fun toString(): String =
      ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
        .append("id", id)
        .append("identifier", identifier)
        .append("firstname", firstname)
        .append("lastname", lastname)
        .append("email", email)
        .append("roles", roles)
        .append("password", "****")
        .toString()
}
