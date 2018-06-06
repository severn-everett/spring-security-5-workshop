package com.example.library.server.security

import com.example.library.server.business.UserService
import org.springframework.context.annotation.Primary
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Primary
@Service
class LibraryUserDetailsService(private val userService: UserService) : UserDetailsService {

  override fun loadUserByUsername(username: String): UserDetails {
    userService.findOneByEmail(username)?.let {
      return LibraryUser(it)
    } ?: throw UsernameNotFoundException("No user found for username $username")
  }

}
