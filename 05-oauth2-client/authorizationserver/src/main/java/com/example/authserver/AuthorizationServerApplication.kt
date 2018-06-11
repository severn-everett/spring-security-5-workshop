package com.example.authserver

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class AuthorizationServerApplication

fun main(args: Array<String>) {
  SpringApplication.run(AuthorizationServerApplication::class.java, *args)
}
