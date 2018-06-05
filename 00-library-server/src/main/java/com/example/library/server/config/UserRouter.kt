package com.example.library.server.config

import com.example.library.server.api.UserHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
open class UserRouter {

  @Bean
  open fun route(userHandler: UserHandler): RouterFunction<ServerResponse> =
      RouterFunctions
        .route(RequestPredicates.GET("/users")
          .and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8)),
            HandlerFunction(userHandler::getAllUsers))
        .andRoute(RequestPredicates.GET("/users/{userId}")
          .and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8)),
            HandlerFunction(userHandler::getUser))
        .andRoute(RequestPredicates.POST("/users")
          .and(RequestPredicates.contentType(MediaType.APPLICATION_JSON_UTF8)),
            HandlerFunction(userHandler::createUser))
        .andRoute(RequestPredicates.DELETE("/users/{userId}")
          .and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8)),
            HandlerFunction(userHandler::deleteUser))

}
