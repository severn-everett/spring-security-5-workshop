package com.example.authserver

import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.Stream

/**
 * Initializes some users with passwords.
 */
@Component
class UserDataInitializer(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

  companion object {
    private val USER_IDENTIFIER = UUID.fromString("c47641ee-e63c-4c13-8cd2-1c2490aee0b3")
    private val CURATOR_IDENTIFIER = UUID.fromString("40c5ad0d-41f7-494b-8157-33fad16012aa")
    private val ADMIN_IDENTIFIER = UUID.fromString("0d2c04f1-e25f-41b5-b4cd-3566a081200f")
  }

  override fun run(vararg args: String?) {
    listOf(
        User(
            USER_IDENTIFIER,
            "Library",
            "User",
            "user@example.com",
            passwordEncoder.encode("user"),
            listOf("USER")
        ),
        User(
            CURATOR_IDENTIFIER,
            "Library",
            "Curator",
            "curator@example.com",
            passwordEncoder.encode("curator"),
            listOf("USER", "CURATOR")
        ),
        User(
            ADMIN_IDENTIFIER,
            "Library",
            "Admin",
            "admin@example.com",
            passwordEncoder.encode("admin"),
            listOf("USER", "ADMIN")
        )
    ).forEach{ userRepository.save(it) }
  }
}
