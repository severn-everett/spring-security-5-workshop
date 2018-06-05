package com.example.library.server.dataaccess

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono
import java.util.*

interface UserRepository: ReactiveMongoRepository<User, UUID> {

  fun findOneByEmail(email: String): Mono<User>

}