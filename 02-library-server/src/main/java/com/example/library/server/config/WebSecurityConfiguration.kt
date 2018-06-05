package com.example.library.server.config

import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

@EnableWebFluxSecurity
open class WebSecurityConfiguration {

  @Bean
  open fun passwordEncoder() : PasswordEncoder =
      PasswordEncoderFactories.createDelegatingPasswordEncoder()

}
