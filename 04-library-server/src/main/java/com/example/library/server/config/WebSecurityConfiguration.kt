package com.example.library.server.config

import com.example.library.server.common.Role
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest
import org.springframework.boot.autoconfigure.security.reactive.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler
import java.net.URI

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
open class WebSecurityConfiguration {

  @Bean
  open fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
    http
      //.csrf().disable()
      .authorizeExchange()
      .matchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
      .matchers(EndpointRequest.to("health")).permitAll()
      .matchers(EndpointRequest.to("info")).permitAll()
      .matchers(EndpointRequest.toAnyEndpoint()).hasRole(Role.ADMIN.name)
      .pathMatchers(HttpMethod.POST, "/books").hasRole(Role.CURATOR.name)
      .pathMatchers(HttpMethod.DELETE, "/books").hasRole(Role.CURATOR.name)
      .pathMatchers("/users/**").hasRole(Role.ADMIN.name)
      .anyExchange().authenticated()
      //.anyExchange().permitAll()
      .and()
      .httpBasic().and().formLogin().and()
      .logout().logoutSuccessHandler(logoutSuccessHandler())
      .and()
      .build()

  @Bean
  open fun passwordEncoder(): PasswordEncoder =
    PasswordEncoderFactories.createDelegatingPasswordEncoder()

  @Bean
  open fun logoutSuccessHandler(): ServerLogoutSuccessHandler =
    RedirectServerLogoutSuccessHandler().apply { setLogoutSuccessUrl(URI.create("/books")) }

}
