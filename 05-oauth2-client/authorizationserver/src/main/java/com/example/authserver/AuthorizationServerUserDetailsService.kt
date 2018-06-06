package com.example.authserver

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * Implementation for [UserDetailsService].
 */
@Service
class AuthorizationServerUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

  override fun loadUserByUsername(userName: String): UserDetails {
    return userRepository.findOneByEmail(userName)
        ?: throw UsernameNotFoundException("No user found for $userName")
  }
}
