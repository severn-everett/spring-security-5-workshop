package com.example.library.server.api

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorHandler {

  @ExceptionHandler
  fun handle(ex: RuntimeException): ResponseEntity<String> {
    val logger = LoggerFactory.getLogger(this.javaClass)
    logger.error(ex.message)
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
  }

  @ExceptionHandler
  fun handle(ex: AccessDeniedException): ResponseEntity<String> {
    val logger = LoggerFactory.getLogger(this.javaClass)
    logger.error(ex.message)
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
  }

  @ExceptionHandler
  fun handle(ex: Exception): ResponseEntity<String> {
    val logger = LoggerFactory.getLogger(this.javaClass)
    logger.error(ex.message)
    return ResponseEntity.badRequest().build()
  }

}
