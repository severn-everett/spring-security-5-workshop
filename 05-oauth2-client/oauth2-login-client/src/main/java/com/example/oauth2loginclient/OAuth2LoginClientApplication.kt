package com.example.oauth2loginclient

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class OAuth2LoginClientApplication

fun main(args: Array<String>) {
  SpringApplication.run(OAuth2LoginClientApplication::class.java, *args)
}
