package com.example.library.server.config

import com.example.library.server.security.LibraryUserAuthenticationConverter
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.provider.token.AccessTokenConverter
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import org.springframework.util.StringUtils

@Configuration
@EnableResourceServer
open class ResourceServerConfiguration(
    private val resource: ResourceServerProperties,
    private val libraryUserAuthenticationConverter: LibraryUserAuthenticationConverter
) : ResourceServerConfigurerAdapter() {

  override fun configure(http: HttpSecurity) {
    http.authorizeRequests()
      .antMatchers(HttpMethod.GET, "/books").hasRole("USER")
      .antMatchers(HttpMethod.POST, "/books").hasRole("CURATOR")
      .antMatchers(HttpMethod.DELETE, "/books").hasRole("CURATOR")
      .antMatchers("/users/**").hasRole("ADMIN")
      .anyRequest().authenticated()
  }

  @Primary
  @Bean
  open fun jwtTokenStore(): TokenStore = JwtTokenStore(libraryJwtTokenEnhancer())

  @Primary
  @Bean
  open fun libraryJwtTokenEnhancer(): JwtAccessTokenConverter = JwtAccessTokenConverter().apply {
    val keyValue = resource.jwt.keyValue
    if (StringUtils.hasText(keyValue) && !keyValue.startsWith("-----BEGIN")) {
      setSigningKey(keyValue)
    }
    setVerifierKey(keyValue)
    accessTokenConverter = libraryAccessTokenConverter()
  }

  @Bean
  open fun libraryAccessTokenConverter(): AccessTokenConverter =
    DefaultAccessTokenConverter().apply { setUserTokenConverter(libraryUserAuthenticationConverter) }

}
