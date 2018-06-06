package com.example.authserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Web security configuration.
 */
@Configuration
@EnableWebSecurity
open class WebSecurityConfiguration(private val userDetailsService: UserDetailsService) : WebSecurityConfigurerAdapter() {

  override fun configure(auth: AuthenticationManagerBuilder) {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
  }

  override fun configure(http: HttpSecurity) {
    http
      .authorizeRequests()
      .anyRequest().authenticated()
      .and()
      .formLogin()
  }

  @Bean
  open fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
