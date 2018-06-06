package com.example.authserver.api

import com.example.authserver.User
import com.example.authserver.UserRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class UserInfoRestController(private val userRepository: UserRepository) {

  @SuppressWarnings("unused")
  @GetMapping("/resources/userinfo")
  fun userInfo(principal: Principal): User? =
      userRepository.findOneByEmail(principal.name)
}
