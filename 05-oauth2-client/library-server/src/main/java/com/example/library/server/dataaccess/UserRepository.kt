package com.example.library.server.dataaccess

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface UserRepository: MongoRepository<User, UUID> {

  fun findOneByEmail(email: String): Optional<User>

}
