package com.example.library.server.api

import com.example.library.server.business.UserResource
import com.example.library.server.business.UserService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

/**
 * Reactive handler for users.
 */
@RestController
@RequestMapping(path = ["/users"])
class UserRestController(private val userService: UserService) {

  @SuppressWarnings("unused")
  @GetMapping
  fun getAllUsers(): ResponseEntity<List<UserResource>> =
      ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(userService.findAll())

  @GetMapping(path = ["/{userId}"])
  fun getUser(@PathVariable("userId") userId: UUID): ResponseEntity<UserResource> =
      userService.findById(userId)?.let {
        ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(it)
      } ?: notFound().build()

  @DeleteMapping(path = ["/{userId}"])
  fun deleteUser(@PathVariable("userId") userId: UUID): ResponseEntity<Void> {
    userService.deleteById(userId)
    return noContent().build()
  }

  @PostMapping
  fun createUser(@Valid @RequestBody userResource: UserResource): ResponseEntity<Void> {
    userService.create(userResource)
    return ok().build()
  }
}
