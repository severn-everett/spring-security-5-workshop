package com.example.oauth2loginclient.config

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.web.client.RestTemplate

@Configuration
open class RestTemplateConfiguration {

  @Bean
  open fun restTemplate(clientService: OAuth2AuthorizedClientService): RestTemplate =
      RestTemplateBuilder()
        .interceptors(
            ClientHttpRequestInterceptor(
                { httpRequest, bytes, execution ->
                  val authentication = SecurityContextHolder.getContext().authentication
                  if (authentication is OAuth2AuthenticationToken) {
                    val client = clientService.loadAuthorizedClient<OAuth2AuthorizedClient>(
                        authentication.authorizedClientRegistrationId, authentication.name
                    )
                    httpRequest.headers
                      .add(HttpHeaders.AUTHORIZATION, "Bearer ${client.accessToken.tokenValue}")
                  }
                  execution.execute(httpRequest, bytes)
                }
            )
        ).build()
}
