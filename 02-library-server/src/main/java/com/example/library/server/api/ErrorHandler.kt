package com.example.library.server.api

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import reactor.core.publisher.Mono

@RestControllerAdvice
class ErrorHandler {

  @ExceptionHandler
  fun handle(ex: RuntimeException): Mono<ResponseEntity<String>> {
    val logger = LoggerFactory.getLogger(this.javaClass)
    logger.error(ex.message)
    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
  }

  @ExceptionHandler
  fun handle(ex: AccessDeniedException): Mono<ResponseEntity<String>> {
    val logger = LoggerFactory.getLogger(this.javaClass)
    logger.error(ex.message)
    return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).build())
  }

  @ExceptionHandler
  fun handle(ex: Exception): Mono<ResponseEntity<String>> {
    val logger = LoggerFactory.getLogger(this.javaClass)
    logger.error(ex.message)
    return Mono.just(ResponseEntity.badRequest().build())
  }

}
