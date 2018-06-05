package com.example.library.server.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.IdGenerator
import org.springframework.util.JdkIdGenerator

@Configuration
open class IdGeneratorConfiguration {

  @Bean
  open fun idGenerator(): IdGenerator = JdkIdGenerator()

}
