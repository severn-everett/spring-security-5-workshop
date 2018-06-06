package com.example.authserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory

@Configuration
@EnableAuthorizationServer
open class AuthorizationServerConfiguration(
    private val authenticationManager: AuthenticationManager
) : AuthorizationServerConfigurerAdapter() {

  override fun configure(clients: ClientDetailsServiceConfigurer) {
    clients
      .inMemory()
      .withClient("library-client")
      .secret("secret")
      .authorizedGrantTypes("authorization_code")
      .scopes("profile")
      .redirectUris("http://localhost:8081/login/oauth2/code/login-client")
  }

  override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
    endpoints
      .tokenStore(this.tokenStore())
      .accessTokenConverter(jwtAccessTokenConverter())
      .authenticationManager(this.authenticationManager)
  }

  @Bean
  open fun jwtAccessTokenConverter(): JwtAccessTokenConverter = JwtAccessTokenConverter().apply {
    val factory = KeyStoreKeyFactory(ClassPathResource("jwtdemo.jks"),
        "secret".toCharArray())
    setKeyPair(factory.getKeyPair("jwtdemo"))
  }

  @Bean
  open fun tokenStore(): TokenStore = JwtTokenStore(jwtAccessTokenConverter())
}
