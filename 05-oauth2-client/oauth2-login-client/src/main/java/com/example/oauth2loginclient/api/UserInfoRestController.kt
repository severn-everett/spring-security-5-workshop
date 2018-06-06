package com.example.oauth2loginclient.api

import org.springframework.http.HttpMethod
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
class UserInfoRestController(
    private val restTemplate: RestTemplate,
    private val clientService: OAuth2AuthorizedClientService
) {

  @GetMapping("/")
  fun userInfo(token: OAuth2AuthenticationToken): UserInfo? {
    val client = clientService.loadAuthorizedClient<OAuth2AuthorizedClient>(
        token.authorizedClientRegistrationId,
        token.name
    )
    val uri = client.clientRegistration
      .providerDetails
      .userInfoEndpoint
      .uri
    val responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null, UserInfo::class.java)
    return responseEntity.body
  }

}
