package com.example.authserver

import org.springframework.data.jpa.repository.JpaRepository

/**
 * Repository for [User].
 */
interface UserRepository : JpaRepository<User, Long> {

  fun findOneByEmail(email: String): User?
}
