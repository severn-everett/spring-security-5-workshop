package com.example.library.server.security

import com.example.library.server.business.UserService
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class LibraryReactiveUserDetailsService(private val userService: UserService) : ReactiveUserDetailsService {

  override fun findByUsername(username: String): Mono<UserDetails> =
    userService.findOneByEmail(username).map(::LibraryUser)

}
