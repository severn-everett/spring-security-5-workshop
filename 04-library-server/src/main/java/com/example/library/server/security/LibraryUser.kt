package com.example.library.server.security

import com.example.library.server.business.UserResource
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails

class LibraryUser(val userResource: UserResource) : UserDetails {

  override fun getAuthorities(): Collection<GrantedAuthority> =
    AuthorityUtils.commaSeparatedStringToAuthorityList(
        userResource.roles
          ?.map { "ROLE_${it.name}" }
          ?.joinToString(",")
            ?: ""
    )

  override fun getPassword(): String? = userResource.password

  override fun getUsername(): String? = userResource.email

  override fun isAccountNonExpired(): Boolean = true

  override fun isAccountNonLocked(): Boolean = true

  override fun isCredentialsNonExpired(): Boolean = true

  override fun isEnabled(): Boolean = true
}
