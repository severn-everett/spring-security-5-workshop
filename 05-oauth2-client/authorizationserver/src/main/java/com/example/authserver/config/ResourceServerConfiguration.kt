package com.example.authserver.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter

@Configuration
@EnableResourceServer
open class ResourceServerConfiguration : ResourceServerConfigurerAdapter() {

  override fun configure(http: HttpSecurity) {
    http
      .antMatcher("/resources/**")
      .authorizeRequests()
      .mvcMatchers("/resources/userinfo").access("#oauth2.hasScope('profile')")
  }

}
